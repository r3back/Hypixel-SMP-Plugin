package me.reb4ck.smp.api.command.commands.setlogo.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.setlogo.AbstractSetLogoCommand;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetLogoCommand extends AbstractSetLogoCommand {
    @Inject
    public SetLogoCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 4) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getServer(player, name, trackID.getValue(), boxUtil);

            setLogo(player, name, server, true, args);
        }else if(args.length == 3) {

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getAllPlayerServers(trackID.getValue(), boxUtil).stream().findFirst().orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            setLogo(player, server.getName(), server, false, args);

        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }
}
