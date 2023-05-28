package me.reb4ck.smp.api.ram;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RamSettings {
    private Map<Integer, Integer> prices;
}
