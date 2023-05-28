package me.reb4ck.smp;

import com.google.inject.Module;
import com.google.inject.*;
import com.google.inject.name.Names;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.factory.AddonsFactory;
import me.reb4ck.smp.api.registry.CommandRegistry;
import me.reb4ck.smp.api.service.*;
import me.reb4ck.smp.base.service.FileServiceImpl;
import me.reb4ck.smp.commands.provider.CommandProvider;
import me.reb4ck.smp.factory.AddonsFactoryImpl;
import me.reb4ck.smp.module.DatabaseModule;
import me.reb4ck.smp.module.InitialModule;
import me.reb4ck.smp.module.ServiceModule;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.base.JacksonPersist;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;

public final class SocketSMP extends JavaPlugin implements Module {
    private static SocketSMP INSTANCE;
    private Injector injector;

    public static SocketSMP getInstance(){
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        try {
            INSTANCE = this;

            Console.init(this);

            this.injector = Guice.createInjector(this, new DatabaseModule(), new InitialModule(), new ServiceModule());

            this.injector.getInstance(FileService.class).prepareDefaultServer();

            Console.sendMessage("Has been enabled!", Console.MessageType.COLORED);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(Player::closeInventory);

        this.injector.getInstance(FileService.class).disable();

        this.injector.getInstance(DockerService.class).close();

        Console.sendMessage("Has been disabled!", Console.MessageType.COLORED);
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(JavaPlugin.class).toInstance(this);

        binder.bind(SocketSMP.class).toInstance(this);

        binder.bind(Server.class).toInstance(this.getServer());

        binder.bind(BukkitScheduler.class).toInstance(this.getServer().getScheduler());

        binder.bind(PluginManager.class).toInstance(this.getServer().getPluginManager());

        binder.bind(File.class).annotatedWith(Names.named("pluginFolder")).toInstance(this.getDataFolder());

        binder.bind(Persist.class).to(JacksonPersist.class).asEagerSingleton();

        binder.bind(FileService.class).to(FileServiceImpl.class).asEagerSingleton();

        binder.bind(AddonsFactory.class).to(AddonsFactoryImpl.class).asEagerSingleton();

        binder.bind(new TypeLiteral<CommandRegistry<CoreCommand>>() {}).to(CommandProvider.class).asEagerSingleton();

    }
}