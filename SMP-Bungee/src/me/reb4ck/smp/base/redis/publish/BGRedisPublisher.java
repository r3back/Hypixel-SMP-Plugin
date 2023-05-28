package me.reb4ck.smp.base.redis.publish;

import com.google.inject.Inject;
import io.lettuce.core.RedisConnectionException;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisConfig;
import me.reb4ck.smp.redis.RedisPublisher;
import me.reb4ck.smp.redis.RedisService;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.URI;
import java.util.logging.Logger;

public final class BGRedisPublisher implements RedisPublisher {
    private final TaskScheduler scheduler;
    private final RedisConfig redisConfig;
    private final Persist persist;
    private final Logger logger;
    private JedisPool jedisPool;
    private final Plugin plugin;

    @Inject
    public BGRedisPublisher(RedisConfig redisConfig, Persist persist, Plugin plugin, TaskScheduler scheduler) {
        this.plugin = plugin;
        this.persist = persist;
        this.scheduler = scheduler;
        this.redisConfig = redisConfig;
        this.logger = plugin.getLogger();

        connect();
    }

    public void connect() throws RedisConnectionException {
        scheduler.runAsync(plugin, () -> {
            try {
                logger.info("Connecting to redis Publisher...");

                this.jedisPool = new JedisPool(new JedisPoolConfig(), URI.create(redisConfig.getUri()), 0);

                logger.info("Successfully connected to Redis Publisher!");

            }catch (Exception ignored){
                logger.info("Failed Creating Redis Publisher Connection.");
            }
        });
    }

    @Override
    public void publish(String channel, String message) {
        scheduler.runAsync(plugin, () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, message);
            } catch (JedisConnectionException ignored) {}
        });
    }

    @Override
    public void publish(String channel, RedisService.Message message) {
        scheduler.runAsync(plugin, () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, persist.toString(message, Persist.PersistType.JSON));
            } catch (JedisConnectionException ignored) {}
        });
    }

    @Override
    public void publishSync(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message);
        } catch (JedisConnectionException ignored) {}
    }
}
