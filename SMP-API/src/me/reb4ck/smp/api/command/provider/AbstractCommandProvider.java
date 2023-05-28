package me.reb4ck.smp.api.command.provider;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.reb4ck.smp.api.command.CommandDetails;
import me.reb4ck.smp.api.command.commands.AddDescriptionLineCommand;
import me.reb4ck.smp.api.command.commands.LobbyMenuCommand;
import me.reb4ck.smp.api.command.commands.RemoveDescriptionLineCommand;
import me.reb4ck.smp.api.command.commands.ResetDescriptionCommand;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.registry.CommandRegistry;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractCommandProvider implements CommandRegistry<CoreCommand> {
    protected final ConfigManager<Configuration, Messages, Commands, Inventories> configFiles;
    protected final List<CoreCommand> commands = new ArrayList<>();
    protected final BukkitScheduler scheduler;
    protected final JavaPlugin plugin;

    /**
     * Default Constructor
     */
    @Inject
    public AbstractCommandProvider(JavaPlugin plugin, ConfigManager<Configuration, Messages, Commands, Inventories> configManager,
                           BukkitScheduler scheduler) {
        plugin.getCommand("smp").setExecutor(this);
        plugin.getCommand("smp").setTabCompleter(this);

        this.plugin = plugin;
        this.configFiles = configManager;
        this.scheduler = scheduler;

    }

    /**
     * Register a single command in the command system
     *
     * @param command The command which should be registered
     */
    @Override
    public void registerCommand(CoreCommand command) {
        commands.add(command);
    }


    @Override
    public List<CoreCommand> getCommands() {
        return commands;
    }

    @Override
    public void reloadCommands(){
        commands.clear();
        registerCommands();
    }

    /**
     * Method which handles command execution for all sub-commands.
     * Automatically checks if a User can execute the command.
     * All parameters are provided by Bukkit.
     *
     * @param commandSender The sender which executes this command
     * @param args          The arguments of this command
     * @return true if this command was executed successfully
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof Player) {
                //commandSender.sendMessage(StringUtils.color(configFiles.messages().useHelp));
                return true;
            }
        }

        for (CoreCommand command : commands) {
            // We don't want to execute other commands or ones that are disabled
            if (!(command.aliases.contains(args[0]) && command.enabled))
                continue;

            if (executionBlocked(command, commandSender))
                return false;

            //boolean success = executingCommand.execute(commandSender, args);

            //if (success)


            scheduler.runTaskAsynchronously(plugin, () -> command.execute(commandSender, args));

            command.getCooldownProvider().applyCooldown(commandSender);

            return true;
        }
        // Unknown commands message
        commandSender.sendMessage(StringUtils.color(configFiles.messages().unknownCommand
                .replace("%prefix%", configFiles.config().prefix)));
        return false;

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // Handle the tab completion if it's a sub-commands.
        if (args.length == 1) {

            List<String> result = new ArrayList<>();
            for (CoreCommand command : commands.stream().filter(coreCommand -> !coreCommand.isExcludedCommand()).collect(Collectors.toList())) {

                for (String alias : command.aliases) {
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && (command.enabled && hasPermission(commandSender, command)))
                        result.add(alias);
                }

            }
            return result;
        }

        // Let the sub-commands handle the tab completion
        for (CoreCommand command : commands) {
            if (command.aliases.contains(args[0]) && (command.enabled && hasPermission(commandSender, command)))
                return command.onTabComplete(commandSender, cmd, label, args);
        }

        return null;
    }



    private boolean hasPermission(CommandSender commandSender, CoreCommand command){
        return (commandSender.hasPermission(command.permission) || command.permission.equalsIgnoreCase("") || command.permission.equalsIgnoreCase("smp."));
    }

    private boolean executionBlocked(CoreCommand command, CommandSender commandSender) {
        // Check if this command is only for players
        if (command.onlyForPlayers && !(commandSender instanceof Player)) {
            // Must be a player
            commandSender.sendMessage(StringUtils.color(configFiles.messages().mustBeAPlayer
                    .replace("%prefix%", configFiles.config().prefix)));
            return true;
        }

        if (!hasPermission(commandSender, command)) {
            // No permissions
            commandSender.sendMessage(StringUtils.color(configFiles.messages().noPermission
                    .replace("%prefix%", configFiles.config().prefix)));
            return true;
        }

        // Check cooldown
        CommandDetails.CooldownProvider<CommandSender> cooldownProvider = command.getCooldownProvider();

        if (commandSender instanceof Player && cooldownProvider.isOnCooldown(commandSender)) {
            Duration remainingTime = cooldownProvider.getRemainingTime(commandSender);
            String formattedTime = formatDuration(configFiles.messages().activeCooldown, remainingTime);
            commandSender.sendMessage(StringUtils.color(formattedTime.replace("%prefix%", configFiles.config().prefix)));
            return true;
        }

        return false;
    }

    public static String formatDuration(String format, Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return format.replace("%hours%", String.valueOf(hours))
                .replace("%minutes%", String.valueOf(minutes))
                .replace("%seconds%", String.valueOf(seconds));
    }

    public Optional<CoreCommand> findExecutingCommand(String[] arguments) {
        if (arguments.length == 0) {
            return Optional.empty();
        }

        for (CoreCommand command : commands) {
            if (command.aliases.contains(arguments[0].toLowerCase())) {
                return Optional.of(command);
            }
        }

        return Optional.empty();
    }

    /*private CoreCommand findExecutingCommand(CoreCommand baseCommand, String[] args) {
        CoreCommand executingCommand = baseCommand;

        // Check for each argument if it's a child of the previous command
        for (int currentArgument = 1; currentArgument < args.length; currentArgument++) {
            Optional<CoreCommand> child = executingCommand.getChildByName(args[currentArgument]);
            if (!child.isPresent()) break;

            executingCommand = child.get();
        }

        return executingCommand;
    }*/

    private List<String> filterTabCompletionResults(List<String> tabCompletion, String[] arguments) {
        return tabCompletion.stream()
                .filter(completion -> completion.toLowerCase().contains(arguments[arguments.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}
