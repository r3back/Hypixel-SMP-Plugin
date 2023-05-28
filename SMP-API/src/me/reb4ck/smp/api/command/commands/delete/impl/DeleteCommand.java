package me.reb4ck.smp.api.command.commands.delete.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.delete.AbstractDeleteCommand;
import me.reb4ck.smp.api.utils.BoxUtil;

import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.api.tracker.ITrackID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class DeleteCommand extends AbstractDeleteCommand implements SMPGetter {
    @Inject
    public DeleteCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1) {
            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getAllPlayerServers(trackID.getValue(), boxUtil).stream()
                    .findFirst()
                    .orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            delete(player, server.getName(), true);
        }else if(args.length == 2) {
            delete(player, args[1], true);
        }else if(args.length == 3 && args[2].equalsIgnoreCase(SerializedGUI.Value.DELETE_SERVER_CODE.getValue())){
            delete(player, args[1], false);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
