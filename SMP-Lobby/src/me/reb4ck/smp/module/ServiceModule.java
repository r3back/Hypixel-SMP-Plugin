package me.reb4ck.smp.module;

import com.google.inject.AbstractModule;
import me.reb4ck.smp.api.reactive.ReactiveService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.service.TaskService;
import me.reb4ck.smp.api.service.communicator.SenderCommunicatorService;
import me.reb4ck.smp.base.reactive.LobbyReactiveService;
import me.reb4ck.smp.base.redislobby.LobbyRedisService;
import me.reb4ck.smp.base.redislobby.config.LobbyRedisConfig;
import me.reb4ck.smp.base.redislobby.publish.LobbyRedisPublisher;
import me.reb4ck.smp.base.service.SMPServiceImpl;
import me.reb4ck.smp.base.service.TaskServiceImpl;
import me.reb4ck.smp.base.service.communicator.SenderCommunicatorServiceImpl;
import me.reb4ck.smp.redis.RedisConfig;
import me.reb4ck.smp.redis.RedisPublisher;
import me.reb4ck.smp.redis.RedisService;

public final class ServiceModule extends AbstractModule {
    @Override
    public void configure(){
        bind(TaskService.class).to(TaskServiceImpl.class).asEagerSingleton();

        bind(RedisService.class).to(LobbyRedisService.class).asEagerSingleton();

        bind(RedisPublisher.class).to(LobbyRedisPublisher.class).asEagerSingleton();

        bind(RedisConfig.class).to(LobbyRedisConfig.class).asEagerSingleton();

        bind(ReactiveService.class).to(LobbyReactiveService.class).asEagerSingleton();

        bind(SMPService.class).to(SMPServiceImpl.class).asEagerSingleton();

        bind(SenderCommunicatorService.class).to(SenderCommunicatorServiceImpl.class).asEagerSingleton();
    }
}
