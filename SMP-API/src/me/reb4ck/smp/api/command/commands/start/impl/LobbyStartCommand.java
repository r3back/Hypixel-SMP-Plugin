package me.reb4ck.smp.api.command.commands.start.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.start.AbstractStartCommand;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.exception.server.AlreadyOnlineException;
import me.reb4ck.smp.api.exception.server.OnlineLimitException;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class LobbyStartCommand extends AbstractStartCommand {
    @Inject
    public LobbyStartCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServer(name).filter(server1 -> server1.getUuid().equals(trackID.getValue())).orElse(null);

            startServer(player, name, server);

        }else if(args.length == 1){
            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServers(trackID.getValue()).stream().findFirst().orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            startServer(player, server.getName(), server);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }
}
