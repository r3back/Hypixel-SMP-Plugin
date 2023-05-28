package me.reb4ck.smp.api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.reb4ck.smp.api.Timer;
import me.reb4ck.smp.api.Timer.TimeType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class ConfigFeature {
    public Timer featureDuration = new Timer(7, TimeType.DAYS);
    public int price = 2500;
}
