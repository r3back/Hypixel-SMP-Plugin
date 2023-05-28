package me.reb4ck.smp.base.redis;

import com.google.inject.Inject;
import me.reb4ck.smp.redis.RedisConfig;

public final class BGRedisConfig implements RedisConfig {

    @Inject
    public BGRedisConfig() {
    }

    @Override
    public String getUri() {
        return "redis://:PASSWORD@IP:PORT";
    }
}
