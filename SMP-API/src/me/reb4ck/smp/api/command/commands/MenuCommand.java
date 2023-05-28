package me.reb4ck.smp.api.command.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.message.GUIMessage;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.utils.MoneyGetter;
import me.reb4ck.smp.utils.PermissionGetter;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.api.gui.GUIList;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.persist.Persist;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class MenuCommand extends CoreCommand implements PermissionGetter, MoneyGetter {
    private final BoxUtil boxUtil;

    @Inject
    public MenuCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().menuCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1) {
            sendMenu(player);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }


    private void sendMenu(Player player){

        try {
            CompletableFuture<String> slots = getFutureFavoriteSlots(player, boxUtil);
            CompletableFuture<String> money = getFutureMoney(player, boxUtil);
            String trackId = boxUtil.smpRestAPI.getTrackId(player.getName()).getValue();

            CompletableFuture.allOf(slots, money).get();

            SerializedGUI gui = SerializedGUI.builder()
                    .integers(new HashMap<String, Integer>(){{
                        put(SerializedGUI.Value.SERVERS_PER_PAGE.getValue(), configManager.config().serverPerPages);
                        put(SerializedGUI.Value.FAVORITE_SLOTS.getValue(), Integer.parseInt(slots.get()));
                    }})
                    .doubles(new HashMap<String, Double>(){{
                        put(SerializedGUI.Value.TOKEN_MANAGER.getValue(), Double.parseDouble(money.get()));
                    }})
                    .strings(new HashMap<String, String>(){{
                        put(SerializedGUI.Value.TRACK_ID.getValue(), trackId);
                    }})

                    .type(GUIList.MAIN_MENU)
                    .build();


            String toSend = boxUtil.persist.toString(gui, Persist.PersistType.JSON);

            boxUtil.redisService.publish(Channel.GUIS.getName(), GUIMessage.builder()
                    .gui(toSend)
                    .player(player.getName())
                    .build());
        } catch (ExecutionException | InterruptedException e) {
            Console.sendMessage("error while waiting for data");
        }

    }
}
