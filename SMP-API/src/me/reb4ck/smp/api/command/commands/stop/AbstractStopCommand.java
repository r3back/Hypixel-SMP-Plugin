package me.reb4ck.smp.api.command.commands.stop;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.exception.server.NotOnlineException;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStopCommand extends CoreCommand {
    protected final BoxUtil boxUtil;

    @Inject
    public AbstractStopCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().stopCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServer(name).filter(server1 -> server1.getUuid().equals(trackID.getValue())).orElse(null);

            stopServer(player, name, server);
        }else if(args.length == 1){
            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServers(trackID.getValue()).stream().findFirst().orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            stopServer(player, server.getName(), server);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    protected void stopServer(Player player, String name, SMPServer server){
        if(server != null) {
            boxUtil.tpService.teleportAllPlayers(server, configManager.config().lobbyServer).thenRun(() -> {
                try {
                    boxUtil.smpService.stopServer(player, server);

                    player.sendMessage(StringUtils.color(configManager.messages().successfullyStopped.replace("%server%", name)));
                } catch (NotOnlineException e) {
                    player.sendMessage(StringUtils.color(configManager.messages().serverIsOff));
                }
            });
        }else
            player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));

    }
}
