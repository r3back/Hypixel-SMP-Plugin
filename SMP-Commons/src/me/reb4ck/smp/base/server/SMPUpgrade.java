package me.reb4ck.smp.base.server;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class SMPUpgrade {
    private int oldRam;
    private int newRam;
}
