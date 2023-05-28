package me.reb4ck.smp.commands.provider;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.reb4ck.smp.api.command.provider.AbstractCommandProvider;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.commands.commands.UpdateCommand;
import me.reb4ck.smp.commands.commands.UpdateFolderCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import java.util.Comparator;

public final class CommandProvider extends AbstractCommandProvider {
    private final Injector injector;

    @Inject
    public CommandProvider(JavaPlugin plugin, Injector injector, ConfigManager<Configuration, Messages, Commands, Inventories> configManager, BukkitScheduler scheduler) {
        super(plugin, configManager, scheduler);
        this.injector = injector;

        registerCommands();
    }

    @Override
    public void registerCommands() {
        registerCommand(injector.getInstance(UpdateCommand.class));
        registerCommand(injector.getInstance(UpdateFolderCommand.class));

        commands.sort(Comparator.comparing(command -> command.aliases.get(0)));
    }
}
