package me.reb4ck.smp.commands.provider;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.reb4ck.smp.api.command.commands.console.impl.ConsoleCommand;
import me.reb4ck.smp.api.command.commands.delete.impl.DeleteCommand;
import me.reb4ck.smp.api.command.commands.delete.impl.LobbyDeleteCommand;
import me.reb4ck.smp.api.command.commands.displayname.impl.LobbySetDisplayNameCommand;
import me.reb4ck.smp.api.command.commands.displayname.impl.SetDisplayNameCommand;
import me.reb4ck.smp.api.command.commands.manage.impl.LobbyManageCommand;
import me.reb4ck.smp.api.command.commands.manage.impl.ManageCommand;
import me.reb4ck.smp.api.command.commands.setlogo.impl.LobbySetLogoCommand;
import me.reb4ck.smp.api.command.commands.setlogo.impl.SetLogoCommand;
import me.reb4ck.smp.api.command.commands.start.impl.LobbyStartCommand;
import me.reb4ck.smp.api.command.commands.start.impl.StartCommand;
import me.reb4ck.smp.api.command.commands.stop.impl.LobbyStopCommand;
import me.reb4ck.smp.api.command.commands.stop.impl.StopCommand;
import me.reb4ck.smp.api.command.provider.AbstractCommandProvider;
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
        registerCommand(injector.getInstance(TeleportCommand.class));
        registerCommand(injector.getInstance(AdminCommand.class));
        registerCommand(injector.getInstance(HelpCommand.class));
        registerCommand(injector.getInstance(LobbyCommand.class));
        registerCommand(injector.getInstance(ResetDescriptionCommand.class));
        registerCommand(injector.getInstance(DescriptionGUICommand.class));

        //Abstract
        registerCommand(injector.getInstance(MenuCommand.class));
        registerCommand(injector.getInstance(ManageCommand.class));
        registerCommand(injector.getInstance(DeleteCommand.class));
        registerCommand(injector.getInstance(StartCommand.class));
        registerCommand(injector.getInstance(SetLogoCommand.class));
        registerCommand(injector.getInstance(SetDisplayNameCommand.class));
        registerCommand(injector.getInstance(StopCommand.class));
        registerCommand(injector.getInstance(ConsoleCommand.class));

        commands.sort(Comparator.comparing(command -> command.aliases.get(0)));
    }
}
