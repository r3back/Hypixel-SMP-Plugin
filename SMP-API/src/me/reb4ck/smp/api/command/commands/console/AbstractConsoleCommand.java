package me.reb4ck.smp.api.command.commands.console;

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

public abstract class AbstractConsoleCommand extends CoreCommand {
    protected final BoxUtil boxUtil;

    @Inject
    public AbstractConsoleCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().consoleCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length >= 2) {

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServers(trackID.getValue()).stream().findFirst().orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            sendCommand(player, server.getName(), server, getMessage(args));
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
    //Settlement setleave <message>
    protected String getMessage(String[] args){
        StringBuilder text = new StringBuilder();

        for(int i = 1 ; i < args.length; i++)
            text
                    .append(args[i])
                    .append(i == args.length - 1 ? "" : " ");


        return text.toString();
    }

    protected void sendCommand(Player player, String name, SMPServer server, String command){
        if(server != null) {
            try {
                boxUtil.smpService.sendCommand(server, command);
            } catch (NotOnlineException e) {
                player.sendMessage(StringUtils.color(configManager.messages().serverIsOff));
            }
        }else
            player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));

    }
}