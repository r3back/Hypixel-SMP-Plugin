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
public final class FavoriteMessage implements RedisService.Message {
    private String server;
    private String name;
    private ActionType actionType;

    public enum ActionType{
        REMOVE,
        ADD
    }
}
