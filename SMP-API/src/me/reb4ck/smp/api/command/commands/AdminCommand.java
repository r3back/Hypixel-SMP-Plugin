package me.reb4ck.smp.api.command.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.message.GUIMessage;
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

public final class AdminCommand extends CoreCommand implements PermissionGetter {
    private final BoxUtil boxUtil;

    @Inject
    public AdminCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().adminCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1) {
            SerializedGUI gui = SerializedGUI.builder()
                    .integers(new HashMap<String, Integer>(){{

                        put(SerializedGUI.Value.SERVER_SLOTS.getValue(), getServerSlots(player, boxUtil));

                        for(Integer amount : boxUtil.reactiveService.getRamPrices().getPrices().keySet())
                            put(SerializedGUI.Value.SERVER_RAM_AMOUNT + String.valueOf(amount) , boxUtil.reactiveService.getRamPrices().getPrices().get(amount));
                    }})
                    .type(GUIList.ADMIN_ALL_SERVERS)
                    .build();

            String toSend = boxUtil.persist.toString(gui, Persist.PersistType.JSON);

            boxUtil.redisService.publish(Channel.GUIS.getName(), GUIMessage.builder()
                    .player(player.getName())
                    .gui(toSend)
                    .build());
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
