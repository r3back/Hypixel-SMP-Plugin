package me.reb4ck.smp.base.redis.publish;

import com.google.inject.Inject;
import io.lettuce.core.RedisConnectionException;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisConfig;
import me.reb4ck.smp.redis.RedisPublisher;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import me.reb4ck.smp.redis.RedisService.Message;

import java.net.URI;
import java.util.logging.Logger;

public final class SocketRedisPublisher implements RedisPublisher {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    private final BukkitScheduler scheduler;
    private final RedisConfig redisConfig;
    private final JavaPlugin plugin;
    private final Persist persist;
    private final Logger logger;
    private JedisPool jedisPool;

    @Inject
    public SocketRedisPublisher(RedisConfig redisConfig, Persist persist, JavaPlugin plugin, BukkitScheduler scheduler, ConfigManager<Configuration, Messages, Commands, Inventories> configManager) {
        this.plugin = plugin;
        this.persist = persist;
        this.scheduler = scheduler;
        this.redisConfig = redisConfig;
        this.logger = plugin.getLogger();
        this.configManager = configManager;

        connect();
    }

    public void connect() throws RedisConnectionException {
        scheduler.runTaskAsynchronously(plugin, () -> {
            try {
                logger.info("Connecting to redis Publisher...");

                if(configManager.config().useCredentialsFromHere)
                    this.jedisPool = new JedisPool(new JedisPoolConfig(), URI.create(redisConfig.getUri()), 0);
                else
                    this.jedisPool = new JedisPool(new JedisPoolConfig(), URI.create(configManager.config().privateRedisUri), 0);

                jedisPool.setMaxTotal(8);

                logger.info("Successfully connected to Redis Publisher!");

            }catch (Exception ignored){
                logger.info("Failed Creating Redis Publisher Connection.");
            }
        });
    }

    @Override
    public void publish(String channel, String message) {
        scheduler.runTaskAsynchronously(plugin, () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, message);
            } catch (JedisConnectionException ignored) {ignored.printStackTrace();}
        });
    }

    @Override
    public void publish(String channel, Message message) {
        scheduler.runTaskAsynchronously(plugin, () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, persist.toString(message, Persist.PersistType.JSON));
            } catch (JedisConnectionException ignored) {ignored.printStackTrace();}
        });
    }

    @Override
    public void publishSync(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message);
        } catch (JedisConnectionException ignored) {ignored.printStackTrace();}
    }
}
