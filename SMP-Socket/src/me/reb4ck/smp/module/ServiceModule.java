package me.reb4ck.smp.module;

import com.google.inject.AbstractModule;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.service.communicator.SenderCommunicatorService;
import me.reb4ck.smp.base.redis.SocketRedisService;
import me.reb4ck.smp.base.redis.config.SocketRedisConfig;
import me.reb4ck.smp.base.redis.publish.SocketRedisPublisher;
import me.reb4ck.smp.base.service.SMPServiceImpl;
import me.reb4ck.smp.base.service.communicator.SenderCommunicatorServiceImpl;
import me.reb4ck.smp.redis.RedisConfig;
import me.reb4ck.smp.redis.RedisPublisher;
import me.reb4ck.smp.redis.RedisService;
import org.bukkit.Bukkit;

public final class ServiceModule extends AbstractModule {
    @Override
    public void configure(){
        //Socket

        bind(RedisService.class).to(SocketRedisService.class).asEagerSingleton();

        bind(RedisConfig.class).to(SocketRedisConfig.class).asEagerSingleton();

        bind(RedisPublisher.class).to(SocketRedisPublisher.class).asEagerSingleton();

        bind(SMPService.class).to(SMPServiceImpl.class).asEagerSingleton();

        bind(SenderCommunicatorService.class).to(SenderCommunicatorServiceImpl.class).asEagerSingleton();

    }
}
