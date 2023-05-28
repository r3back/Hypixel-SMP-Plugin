package me.reb4ck.smp.api.command.commands.displayname.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.displayname.AbstractDisplayNameCommand;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.api.tracker.ITrackID;;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
public final class SetDisplayNameCommand extends AbstractDisplayNameCommand implements SMPGetter {

    @Inject
    public SetDisplayNameCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length >= 2){
            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getAllPlayerServers(trackID.getValue(), boxUtil).stream().findFirst().orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            setDisplayName(player, server.getName(), server, false, args);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
