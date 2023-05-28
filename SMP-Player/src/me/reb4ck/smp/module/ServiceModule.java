package me.reb4ck.smp.module;

import com.google.inject.AbstractModule;
import me.reb4ck.smp.api.reactive.ReactiveService;
import me.reb4ck.smp.api.service.communicator.SenderCommunicatorService;
import me.reb4ck.smp.base.reactive.ReactiveServiceImpl;
import me.reb4ck.smp.base.redis.PlayerRedisService;
import me.reb4ck.smp.base.redis.config.BKRedisConfig;
import me.reb4ck.smp.base.redis.publish.PlayerRedisPublisher;
import me.reb4ck.smp.base.service.communicator.SenderCommunicatorServiceImpl;
import me.reb4ck.smp.redis.RedisConfig;
import me.reb4ck.smp.redis.RedisPublisher;
import me.reb4ck.smp.redis.RedisService;

public final class ServiceModule extends AbstractModule {
    @Override
    public void configure(){
        bind(RedisService.class).to(PlayerRedisService.class).asEagerSingleton();

        bind(RedisPublisher.class).to(PlayerRedisPublisher.class).asEagerSingleton();

        bind(RedisConfig.class).to(BKRedisConfig.class).asEagerSingleton();

        bind(ReactiveService.class).to(ReactiveServiceImpl.class).asEagerSingleton();

        bind(SenderCommunicatorService.class).to(SenderCommunicatorServiceImpl.class).asEagerSingleton();
    }
}
