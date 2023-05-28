package me.reb4ck.smp.api.command.commands.start.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.start.AbstractStartCommand;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.StringUtils;

import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.api.tracker.ITrackID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class StartCommand extends AbstractStartCommand implements SMPGetter {
    @Inject
    public StartCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getServer(player, name, trackID.getValue(), boxUtil);

            startServer(player, name, server);
        }else if(args.length == 1){
            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getAllPlayerServers(trackID.getValue(), boxUtil).stream().findFirst().orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            startServer(player, server.getName(), server);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }
}
