package me.reb4ck.smp.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import me.reb4ck.smp.redis.RedisService;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CommandMessage implements RedisService.Message {
    private Sender sender;
    private String command;
    private String senderName;
    private String receiver;

    public enum Sender{
        PLAYER,
        CONSOLE
    }
}
