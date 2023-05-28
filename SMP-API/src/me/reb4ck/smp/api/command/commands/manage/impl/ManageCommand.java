package me.reb4ck.smp.api.command.commands.manage.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.manage.AbstractManageCommand;

import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.api.utils.BoxUtil;

import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.utils.MoneyGetter;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class ManageCommand extends AbstractManageCommand implements MoneyGetter {
    @Inject
    public ManageCommand(BoxUtil boxUtil){
        super(boxUtil);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            String name = args[1];

            double money = getMoney(player, boxUtil);

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getServer(player, name, trackID.getValue(), boxUtil);

            manage(player, name, money, server);

        }else if(args.length == 1){
            String name = null;

            double money = getMoney(player, boxUtil);

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            Optional<SMPServer> server = getAllPlayerServers(trackID.getValue(), boxUtil).stream().findAny();

            if(!server.isPresent()){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            manage(player, name, money, server.orElse(null));
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

}
