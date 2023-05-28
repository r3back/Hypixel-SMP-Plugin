package me.reb4ck.smp.commands.provider;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.reb4ck.smp.api.command.commands.console.impl.LobbyConsoleCommand;
import me.reb4ck.smp.api.command.commands.delete.impl.LobbyDeleteCommand;
import me.reb4ck.smp.api.command.commands.displayname.impl.LobbySetDisplayNameCommand;
import me.reb4ck.smp.api.command.commands.displayname.impl.SetDisplayNameCommand;
import me.reb4ck.smp.api.command.commands.manage.impl.LobbyManageCommand;
import me.reb4ck.smp.api.command.commands.setlogo.impl.LobbySetLogoCommand;
import me.reb4ck.smp.api.command.commands.setlogo.impl.SetLogoCommand;
import me.reb4ck.smp.api.command.commands.start.impl.LobbyStartCommand;
import me.reb4ck.smp.api.command.commands.stop.impl.LobbyStopCommand;
import me.reb4ck.smp.api.command.commands.stop.impl.StopCommand;
import me.reb4ck.smp.api.command.provider.AbstractCommandProvider;
import me.reb4ck.smp.commands.commands.*;
import me.reb4ck.smp.api.command.commands.*;
import me.reb4ck.smp.base.config.*;
import me.reb4ck.smp.api.config.ConfigManager;
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
        registerCommand(injector.getInstance(AddDescriptionLineCommand.class));
        registerCommand(injector.getInstance(RemoveDescriptionLineCommand.class));
        registerCommand(injector.getInstance(ReloadCommand.class));
        registerCommand(injector.getInstance(CreateCommand.class));
        registerCommand(injector.getInstance(TeleportCommand.class));
        registerCommand(injector.getInstance(AdminCommand.class));
        registerCommand(injector.getInstance(HelpCommand.class));
        registerCommand(injector.getInstance(LobbyCommand.class));
        registerCommand(injector.getInstance(ForceDeleteCommand.class));
        registerCommand(injector.getInstance(DeleteAllCommand.class));
        registerCommand(injector.getInstance(ResetDescriptionCommand.class));
        registerCommand(injector.getInstance(DescriptionGUICommand.class));

        //Abstract
        registerCommand(injector.getInstance(LobbyMenuCommand.class));
        registerCommand(injector.getInstance(LobbyManageCommand.class));
        registerCommand(injector.getInstance(LobbyDeleteCommand.class));
        registerCommand(injector.getInstance(LobbyStartCommand.class));
        registerCommand(injector.getInstance(LobbySetLogoCommand.class));
        registerCommand(injector.getInstance(LobbySetDisplayNameCommand.class));
        registerCommand(injector.getInstance(LobbyStopCommand.class));
        registerCommand(injector.getInstance(LobbyConsoleCommand.class));


        commands.sort(Comparator.comparing(command -> command.aliases.get(0)));
    }
}
