package me.reb4ck.smp.api.command.commands.start;

import com.google.inject.Inject;
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

public abstract class AbstractStartCommand extends CoreCommand {
    protected final BoxUtil boxUtil;

    @Inject
    public AbstractStartCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().startCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getServer(player, name, trackID.getValue(), boxUtil);

            startServer(player, name, server);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    protected void startServer(Player player, String name, SMPServer server){
        try {
            if(server != null) {
                boxUtil.smpService.startServer(server);
                player.sendMessage(StringUtils.color(configManager.messages().successfullyStarted.replace("%server%", name)));
            }else
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));
        } catch (AlreadyOnlineException e) {
            player.sendMessage(StringUtils.color(configManager.messages().serverIsOn));
        }catch (OnlineLimitException e){
            player.sendMessage(StringUtils.color(configManager.messages().serverLimitReach));
        }
    }
}