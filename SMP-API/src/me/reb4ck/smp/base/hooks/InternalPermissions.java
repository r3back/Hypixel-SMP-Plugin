package me.reb4ck.smp.base.hooks;

import com.google.inject.Singleton;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@Singleton
public final class InternalPermissions implements PermissionsPlugin<Player> {
    @Override
    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean hasDefaultPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean hasDefaultPermission(String name, String permission) {
        return Optional.ofNullable(Bukkit.getPlayer(name))
                .map(player -> player.hasPermission(permission))
                .orElse(false);
    }

    @Override
    public int getFavoriteSlots(String Player) {
        return 1;
    }

    @Override
    public int getServerSlots(String Player) {
        return 1;
    }
}
