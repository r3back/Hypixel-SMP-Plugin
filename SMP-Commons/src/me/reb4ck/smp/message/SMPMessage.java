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
public final class SMPMessage implements RedisService.Message {
    private String smpServer;
    private ActionType actionType;
    private int newRam;

    public enum ActionType {
        CREATE,
        DELETE,
        STOP,
        START,
        TELEPORT,
        UPDATE,
        UPDATE_VIRTUAL
    }
}
