package me.reb4ck.smp.api.service;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.api.hooks.EconomyPlugin;
import me.reb4ck.smp.api.hooks.PlaceholdersPlugin;
import me.reb4ck.smp.base.service.AddonsServiceImpl;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import org.bukkit.entity.Player;

@ImplementedBy(AddonsServiceImpl.class)
public interface AddonsService {
    PermissionsPlugin<Player> getPermissions();
    PlaceholdersPlugin getPlaceholders();
    EconomyPlugin getEconomy();
}
