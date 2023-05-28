package me.reb4ck.smp.api.command;

import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.List;

@NoArgsConstructor
public final class CommandConfig extends CommandDetails {
    public CommandConfig(List<String> aliases, String description, String syntax, String permission, boolean onlyForPlayers, Duration cooldown, boolean enabled) {
        super(aliases, description, syntax, permission, onlyForPlayers, cooldown, enabled);
    }
}
