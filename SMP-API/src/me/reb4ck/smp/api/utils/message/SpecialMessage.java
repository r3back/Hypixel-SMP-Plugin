package me.reb4ck.smp.api.utils.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public final class SpecialMessage {
    public List<String> message;
    public String action;
    public String aboveMessage;

    @JsonIgnore
    public SpecialMessage(String message){
        this.message = Collections.singletonList(message);
    }

    public SpecialMessage(List<String> message, String action, String aboveMessage) {
        this.message = message;
        this.action = action;
        this.aboveMessage = aboveMessage;
    }
}

