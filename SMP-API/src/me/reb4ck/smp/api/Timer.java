package me.reb4ck.smp.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public final class Timer{
    private int amount;
    private TimeType type;

    @JsonIgnore
    public long getEffectiveTime(){
        switch (type){
            case MINUTES:
                return Duration.ofMinutes(amount).toMillis();
            case DAYS:
                return Duration.ofDays(amount).toMillis();
            case HOURS:
                return Duration.ofHours(amount).toMillis();
            case SECONDS:
                return Duration.ofSeconds(amount).toMillis();
        }
        return 0L;
    }

    public enum TimeType{
        MINUTES,
        HOURS,
        SECONDS,
        DAYS,
    }
}