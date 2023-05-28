package me.reb4ck.smp.commands.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.service.TeleportService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class ForceDeleteCommand extends CoreCommand {
    private final TeleportService teleportService;
    private final SMPService serverService;

    @Inject
    public ForceDeleteCommand(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, SMPService serverService, TeleportService teleportService) {
        super(configManager.commands().forceDeleteCommand, configManager);
        this.teleportService = teleportService;
        this.serverService = serverService;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            delete(player, args[1]);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    private void delete(Player player, String name){
        Optional<SMPServer> smpServer = serverService.getServer(name);

        if (!smpServer.isPresent()) {
            player.sendMessage(StringUtils.color(configManager.messages().serverNamedDoesntExist.replace("%server%", name)));
            return;
        }

        try {
            serverService.deleteServer(smpServer.get());

            player.sendMessage(StringUtils.color(configManager.messages().successfullyDeleted.replace("%server%", name)));
        }catch (Exception e){
            e.printStackTrace();
            player.sendMessage(StringUtils.color(configManager.messages().errorDeleting.replace("%server%", smpServer.get().getName())));
        }
    }
}
