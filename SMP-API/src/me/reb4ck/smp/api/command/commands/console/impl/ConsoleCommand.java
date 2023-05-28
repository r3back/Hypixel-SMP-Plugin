package me.reb4ck.smp.api.command.commands.console.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.console.AbstractConsoleCommand;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ConsoleCommand extends AbstractConsoleCommand implements SMPGetter {
    @Inject
    public ConsoleCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length >= 2) {

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getAllPlayerServers(trackID.getValue(), boxUtil).stream().findFirst().orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            sendCommand(player, server.getName(), server, getMessage(args));
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }
}
