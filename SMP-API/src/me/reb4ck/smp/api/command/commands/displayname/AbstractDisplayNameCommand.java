package me.reb4ck.smp.api.command.commands.displayname;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class AbstractDisplayNameCommand extends CoreCommand {
    protected final BoxUtil boxUtil;

    @Inject
    public AbstractDisplayNameCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().setDisplayNameCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        /*if(args.length >= 3) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServer(name).filter(server1 -> server1.getUuid().equals(trackID.getValue())).orElse(null);

            setDisplayName(player, name, server, true, args);
        }else */
        if(args.length >= 2){
            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServers(trackID.getValue()).stream().findFirst().orElse(null);

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

    protected void setDisplayName(Player player, String name, SMPServer server, boolean withName, String... args){
        if(server == null){
            player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));
        }else{
            String displayName = withName ? getDescWithName(args) : getDescWithoutName(args);

            if(displayName.length() > 20){
                player.sendMessage(StringUtils.color(configManager.messages().exceedDisplayNameLimit));
                return;
            }

            server.setDisplayName(displayName);

            player.sendMessage(StringUtils.color(configManager.messages().successfullyUpdatedDisplayName));

            boxUtil.redisService.publishUpdate(server);
        }
    }

    private String getDescWithName(String[] args){
        StringBuilder str = new StringBuilder();

        for(int i = 2 ; i < args.length; i++)
            str
                    .append(args[i])
                    .append(i == args.length - 1 ? "" : " ");

        return str.toString();
    }

    private String getDescWithoutName(String[] args){
        StringBuilder str = new StringBuilder();

        for(int i = 1 ; i < args.length; i++)
            str
                    .append(args[i])
                    .append(i == args.length - 1 ? "" : " ");

        return str.toString();
    }
}
