package me.reb4ck.smp.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Channel {
    GUIS("smp:guis"),
    COMMANDS("smp:commands"),
    TELEPORT_REQUESTS("smp:tp"),
    REMOVE_FROM_BUNGEE("smp:rmfbg"),
    BUKKIT_TO_BUNGEE_FAVORITES("smp:fav"),
    BUKKIT_TO_BUNGEE_MESSAGE("smp:fav"),
    BUKKIT_TO_BUNGEE_COMMAND("smp:bktobg"),
    BUKKIT_TO_BUNGEE_UPGRADE("smp:bktobgup"),
    BUKKIT_TO_BUNGEE_REQUEST("smp:bktobgrq"),
    BUKKIT_TO_BUNGEE_TM("smp:bktobgrqtm"),
    BUNGEE_REQUESTS("smp:bungee"),
    SMP_FUTURES_REQUESTS("smp:futuresr"),
    SMP_FUTURES_ANSWERS("smp:futuresa"),
    SMP_UPDATES("smp:updates");

    private final String name;
}
