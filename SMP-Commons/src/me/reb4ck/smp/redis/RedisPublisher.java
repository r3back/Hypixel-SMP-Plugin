package me.reb4ck.smp.redis;

import me.reb4ck.smp.redis.RedisService.Message;

public interface RedisPublisher {
    void publish(String channel, String message);

    void publish(String channel, Message message);

    void publishSync(String channel, String message);
}
