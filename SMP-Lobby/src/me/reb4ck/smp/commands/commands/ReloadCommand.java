package me.reb4ck.smp.commands.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.registry.CommandRegistry;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public final class ReloadCommand extends CoreCommand {
    private final CommandRegistry<CoreCommand> commandRegistry;

    @Inject
    public ReloadCommand(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, CommandRegistry<CoreCommand> commandRegistry) {
        super(configManager.commands().reloadCommand, configManager);
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 1) {
            configManager.reloadFiles();
            commandRegistry.reloadCommands();
            sender.sendMessage(StringUtils.color(configManager.messages().successfullyReloaded));
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
