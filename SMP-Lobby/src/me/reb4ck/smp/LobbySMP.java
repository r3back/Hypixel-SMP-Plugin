package me.reb4ck.smp;

import com.google.inject.*;
import com.google.inject.name.Names;
import me.reb4ck.smp.api.factory.AddonsFactory;
import me.reb4ck.smp.api.service.communicator.SenderCommunicatorService;
import me.reb4ck.smp.base.hooks.LuckPermsPermissions;
import me.reb4ck.smp.base.service.communicator.SenderCommunicatorServiceImpl;
import me.reb4ck.smp.factory.AddonsFactoryImpl;
import me.reb4ck.smp.listener.*;
import me.reb4ck.smp.module.*;

import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import me.reb4ck.smp.api.service.DatabaseService;
import me.reb4ck.smp.redis.RedisService;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.*;

public final class LobbySMP extends JavaPlugin implements Module {
    private Injector injector;

    @Override
    public void onEnable() {
        try {
            Console.init(this);

            this.injector = Guice.createInjector(this, new DatabaseModule(), new InitialModule(), new ServiceModule());

            registerListeners(Arrays.asList(BungeeCordListener.class, SMPListener.class, InventoryClickListener.class));

            Console.sendMessage("Has been enabled!", Console.MessageType.COLORED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        Optional.ofNullable(injector.getInstance(DatabaseService.class)).ifPresent(DatabaseService::disable);

        injector.getInstance(RedisService.class).disable();

        Console.sendMessage("Has been disabled!", Console.MessageType.COLORED);
    }

    @Override
    public void configure(Binder binder) {
        try {
            binder.bind(JavaPlugin.class).toInstance(this);

            binder.bind(LobbySMP.class).toInstance(this);

            binder.bind(Server.class).toInstance(this.getServer());

            binder.bind(BukkitScheduler.class).toInstance(this.getServer().getScheduler());

            binder.bind(AddonsFactory.class).to(AddonsFactoryImpl.class).asEagerSingleton();

            binder.bind(File.class).annotatedWith(Names.named("pluginFolder")).toInstance(this.getDataFolder());

            binder.bind(new TypeLiteral<PermissionsPlugin<Player>>() {}).to(LuckPermsPermissions.class).asEagerSingleton();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void registerListeners(List<Class<? extends Listener>> listeners){
        PluginManager manager = getServer().getPluginManager();
        listeners.forEach(listener -> manager.registerEvents(injector.getInstance(listener), this));
    }
}