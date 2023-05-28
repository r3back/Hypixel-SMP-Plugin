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
public final class GUIMessage implements RedisService.Message {
    private String gui;
    private String player;
}
