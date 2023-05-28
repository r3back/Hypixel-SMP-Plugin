package me.reb4ck.smp.api.registry;

import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;

import java.util.List;
public interface CommandRegistry<T> extends TabExecutor, TabCompleter {
    /**
     * Register Commands
     */
    void registerCommands();

    /**
     * Register a command
     *
     * @param command CoreCommand to be registered
     */
    void registerCommand(T command);

    /**
     * Reload commands
     *
     */
    void reloadCommands();

    /**
     *
     * @return Commands
     */
    List<T> getCommands();
}

