package me.reb4ck.smp.base.hooks;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.Map;

public final class LuckPermsPermissions implements PermissionsPlugin<Player> {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;

    @Inject
    public LuckPermsPermissions(ConfigManager<Configuration, Messages, Commands, Inventories> configManager) {
        this.configManager = configManager;
    }

    /**
     * Check if player has permissions also checks for op permissions
     *
     * @param player     ProxiedPlayer
     * @param permission Permissions
     * @return If player has permission
     */
    @Override
    public boolean hasPermission(Player player, String permission) {
        if(player.hasPermission("*") || player.hasPermission("smp.*")) return true;

        return hasDefaultPermission(player, permission);
    }

    /**
     * Check if player has permission.
     *
     * @param player     ProxiedPlayer
     * @param permission Permissions
     * @return If player has permission
     */
    @Override
    public boolean hasDefaultPermission(Player player, String permission) {
        return hasDefaultPermission(player.getName(), permission);
    }

    @Override
    public boolean hasDefaultPermission(String name, String permission) {
        LuckPerms luckPerms = LuckPermsProvider.get();

        User user = luckPerms.getUserManager().getUser(name);

        if(user == null) return false;

        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }


    @Override
    public int getFavoriteSlots(String name) {
        return getResult(name, configManager.config().initialFavoriteSlots, configManager.config().favoritePerPermission);
    }

    @Override
    public int getServerSlots(String name) {
        return getResult(name, configManager.config().initialServerSlots, configManager.config().serverAmountPerPermission);
    }

    private Integer getResult(String player, int initialAmount, Map<String, Integer> permissions){
        for(String permission : permissions.keySet()){
            if(!hasDefaultPermission(player, permission)) continue;

            int newRam = permissions.get(permission);

            if(newRam > initialAmount) initialAmount = newRam;
        }

        return initialAmount;
    }
}
