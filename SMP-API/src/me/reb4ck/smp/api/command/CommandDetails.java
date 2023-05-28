package me.reb4ck.smp.api.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import me.reb4ck.smp.utils.Console;
import org.bukkit.command.CommandSender;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public abstract class CommandDetails {
    public List<String> aliases;
    public String description;
    public String permission;
    public String syntax;
    public boolean onlyForPlayers;
    public long cooldownInSeconds;
    @JsonIgnore
    public CooldownProvider<CommandSender> cooldownProvider;
    public boolean enabled;

    public CommandDetails(List<String> aliases, String description, String syntax, String permission, boolean onlyForPlayers, Duration duration, boolean enabled) {
        this.aliases = aliases;
        this.description = description;
        this.syntax = syntax;
        this.permission = permission;
        this.onlyForPlayers = onlyForPlayers;
        this.cooldownInSeconds = duration.getSeconds();
        this.enabled = enabled;
    }

    @JsonIgnore
    public CooldownProvider<CommandSender> getCooldownProvider() {
        if(cooldownProvider == null)
            this.cooldownProvider = CooldownProvider.newInstance(Duration.ofSeconds(cooldownInSeconds));
        return cooldownProvider;
    }

    public static class CooldownProvider<T>{
        private final Map<T, Duration> cooldownTimes = new HashMap<>();
        private final String name; // Required for database support
        private final Duration duration;
        private final boolean persistent; // Required for database support

        /**
         * The default constructor.
         *
         * @param name          The name of this CooldownProvider in the database
         * @param duration      The duration which cooldowns from this provider have.
         * @param persistent    True if the cooldowns of this provider should be saved to the database
         */
        private CooldownProvider(String name, Duration duration, boolean persistent) {
            this.name = null;
            this.duration = duration;
            this.persistent = persistent;
        }

        /**
         * Returns if the specified entity has a valid cooldown.
         *
         * @param t The entity which should be checked
         * @return  True if the entity has a valid cooldown
         */
        public boolean isOnCooldown(T t) {
            return cooldownTimes.containsKey(t) && cooldownTimes.get(t).toMillis() > System.currentTimeMillis();
        }

        /**
         * Returns the remaining duration of the cooldown for the provided entity.
         * ZERO if there is no cooldown.
         *
         * @param t The entity which should be checked
         * @return  The duration of the cooldown, can be ZERO
         */
        public Duration getRemainingTime(T t) {
            if (!isOnCooldown(t)) return Duration.ZERO;

            return cooldownTimes.get(t).minusMillis(System.currentTimeMillis());
        }

        /**
         * Reset the cooldown of the specified entity with the duration of this CooldownProvider.
         * {@link CooldownProvider#isOnCooldown(Object)} will return true after this.
         *
         * @param t The entity which should be checked
         */
        public void applyCooldown(T t) {
            cooldownTimes.put(t, duration.plusMillis(System.currentTimeMillis()));
        }

        /**
         * Creates a new CooldownProvider and returns it.
         * It will have the specified cooldown duration.
         *
         * @param duration The duration of the new cooldown
         * @param <T>      The type that can have this cooldown
         * @return         The new CooldownProvider
         */
        public static <T> CooldownProvider<T> newInstance(Duration duration) {
            return new CooldownProvider<>(null, duration, false);
        }

        /**
         * Creates a new CooldownProvider and returns it.
         * It will have the specified cooldown duration.
         *
         * @param name     The name of this CooldownProvider in the database
         * @param duration The duration of the new cooldown
         * @param <T>      The type that can have this cooldown
         * @return         The new CooldownProvider
         */
        public static <T> CooldownProvider<T> newPersistentInstance(String name, Duration duration) {
            throw new NotImplementedException();
            // return new CooldownManager<>(name, duration, true);
        }

    }
}
