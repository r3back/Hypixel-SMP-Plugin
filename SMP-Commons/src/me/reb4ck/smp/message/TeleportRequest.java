package me.reb4ck.smp.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import me.reb4ck.smp.redis.RedisService;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TeleportRequest implements RedisService.Message {
    private String server;
    private String player;
    private Type type;

    public enum Type{
        SERVER,
        SMP,
        ALL
    }
}
