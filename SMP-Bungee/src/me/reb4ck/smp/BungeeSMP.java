package me.reb4ck.smp;

import com.google.inject.*;
import com.google.inject.name.Names;
import me.reb4ck.smp.base.redis.BGRedisConfig;
import me.reb4ck.smp.base.redis.BGRedisService;
import me.reb4ck.smp.base.redis.publish.BGRedisPublisher;

import me.reb4ck.smp.persist.JacksonPersist;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisConfig;
import me.reb4ck.smp.redis.RedisPublisher;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.redis.RedisService;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.io.File;

public final class BungeeSMP extends Plugin implements Module {
    private Injector injector;

    @Override
    public void onEnable() {
        try {
            Console.init(this);

            this.injector = Guice.createInjector(this);

            Console.sendMessage("Has been enabled!", Console.MessageType.COLORED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        injector.getInstance(RedisService.class).disable();

        Console.sendMessage("Has been disabled!", Console.MessageType.COLORED);
    }

    @Override
    public void configure(Binder binder) {
        try {
            binder.bind(Plugin.class).toInstance(this);

            binder.bind(BungeeSMP.class).toInstance(this);

            binder.bind(ProxyServer.class).toInstance(ProxyServer.getInstance());

            binder.bind(TaskScheduler.class).toInstance(this.getProxy().getScheduler());

            binder.bind(File.class).annotatedWith(Names.named("pluginFolder")).toInstance(this.getDataFolder());

            binder.bind(RedisService.class).to(BGRedisService.class).asEagerSingleton();

            binder.bind(RedisPublisher.class).to(BGRedisPublisher.class).asEagerSingleton();

            binder.bind(Persist.class).to(JacksonPersist.class).asEagerSingleton();

            binder.bind(RedisConfig.class).to(BGRedisConfig.class).asEagerSingleton();

            binder.bind(RedisPublisher.class).to(BGRedisPublisher.class).asEagerSingleton();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}