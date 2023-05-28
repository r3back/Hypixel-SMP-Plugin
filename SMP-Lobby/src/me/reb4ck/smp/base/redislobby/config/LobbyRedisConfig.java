package me.reb4ck.smp.base.redislobby.config;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.redis.RedisConfig;

public final class LobbyRedisConfig implements RedisConfig {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;

    @Inject
    public LobbyRedisConfig(ConfigManager<Configuration, Messages, Commands, Inventories> configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getUri() {
        return configManager.config().redisUri;
    }
}
