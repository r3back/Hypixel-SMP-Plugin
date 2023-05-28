package me.reb4ck.smp.api.command.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.reb4ck.smp.api.command.CommandDetails;
import me.reb4ck.smp.api.command.commands.AddDescriptionLineCommand;
import me.reb4ck.smp.api.command.commands.RemoveDescriptionLineCommand;
import me.reb4ck.smp.api.command.commands.ResetDescriptionCommand;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.utils.SMPGetter;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.util.List;

public abstract class CoreCommand extends CommandDetails implements SMPGetter {
    public ConfigManager<Configuration, Messages, Commands, Inventories> configManager;

    public CoreCommand(CommandDetails commandDetails, ConfigManager<Configuration, Messages, Commands, Inventories> configManager) {
        super(commandDetails.aliases, commandDetails.description, commandDetails.syntax, commandDetails.permission, commandDetails.onlyForPlayers, Duration.ofSeconds(commandDetails.cooldownInSeconds), commandDetails.enabled);
        this.configManager = configManager;
    }

    public abstract void execute(CommandSender sender, String[] arguments);

    public abstract List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args);

    @JsonIgnore
    public boolean isExcludedCommand(){
        return this instanceof AddDescriptionLineCommand ||
                this instanceof RemoveDescriptionLineCommand ||
                this instanceof ResetDescriptionCommand;
    }
}
