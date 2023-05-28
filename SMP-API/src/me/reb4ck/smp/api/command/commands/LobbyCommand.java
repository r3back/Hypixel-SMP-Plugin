package me.reb4ck.smp.api.command.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.service.TeleportService;
import me.reb4ck.smp.base.config.*;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class LobbyCommand extends CoreCommand {
    private final TeleportService teleportService;

    @Inject
    public LobbyCommand(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, TeleportService teleportService) {
        super(configManager.commands().lobbyCommand, configManager);
        this.teleportService = teleportService;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1)
            teleportService.teleport(player, configManager.config().lobbyServer);
        else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
