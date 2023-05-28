package me.reb4ck.smp.commands.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class DeleteAllCommand extends CoreCommand {
    private final SMPService serverService;
    private final BoxUtil util;

    @Inject
    public DeleteAllCommand(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, SMPService serverService, BoxUtil util) {
        super(configManager.commands().deleteAllCommand, configManager);
        this.serverService = serverService;
        this.util = util;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;


        if(args.length == 2){
            if(args[1].equalsIgnoreCase(SerializedGUI.Value.DELETE_ALL_SERVER_CODE.getValue()))
                delete(player);
            else
                sender.sendMessage(StringUtils.color(configManager.messages().invalidKey.replace("%prefix%", configManager.config().prefix)));
        }else{
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    private void delete(Player player){
        int i = 0;

        for(SMPServer smpServer : serverService.getServers()){
            try {
                serverService.deleteServer(smpServer);
                player.sendMessage(StringUtils.color(configManager.messages().successfullyDeleted.replace("%server%", smpServer.getName())));
                i++;
            }catch (Exception e){
                e.printStackTrace();
                player.sendMessage(StringUtils.color(configManager.messages().errorDeleting.replace("%server%", smpServer.getName())));
            }
        }

        player.sendMessage(StringUtils.color(configManager.messages().successfullyDeletedAll.replace("%amount%", String.valueOf(i))));
    }
}
