package me.reb4ck.smp.api.command.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.gui.GUIList;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.handler.GUIHandler;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.gui.main.LobbyMainMenuGUI;
import me.reb4ck.smp.message.GUIMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.utils.MoneyGetter;
import me.reb4ck.smp.utils.PermissionGetter;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class LobbyMenuCommand extends CoreCommand implements PermissionGetter, MoneyGetter {
    private final BoxUtil boxUtil;
    private final GUIHandler guiHandler;

    @Inject
    public LobbyMenuCommand(BoxUtil boxUtil, GUIHandler guiHandler) {
        super(boxUtil.config.commands().menuCommand, boxUtil.config);
        this.boxUtil = boxUtil;
        this.guiHandler = guiHandler;
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

        int slots = boxUtil.addonsService.getPermissions().getFavoriteSlots(player.getName());
        String trackId = boxUtil.smpRestAPI.getTrackId(player.getName()).getValue();
        double money = boxUtil.addonsService.getEconomy().getMoney(player.getName());

        SerializedGUI gui = SerializedGUI.builder()
                .integers(new HashMap<String, Integer>(){{
                    put(SerializedGUI.Value.SERVERS_PER_PAGE.getValue(), configManager.config().serverPerPages);
                    put(SerializedGUI.Value.FAVORITE_SLOTS.getValue(), slots);
                }})
                .booleans(new HashMap<String, Boolean>(){{
                    put(SerializedGUI.Value.ALL_SERVERS.getValue(), true);
                }})
                .doubles(new HashMap<String, Double>(){{
                    put(SerializedGUI.Value.TOKEN_MANAGER.getValue(), money);
                }})
                .strings(new HashMap<String, String>(){{
                    put(SerializedGUI.Value.TRACK_ID.getValue(), trackId);
                }})
                .type(GUIList.MAIN_MENU)
                .build();


        boxUtil.scheduler.runTask(boxUtil.plugin, () -> player.openInventory(new LobbyMainMenuGUI(boxUtil, player.getName(), gui, true).getInventory()));
    }
}
