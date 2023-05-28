package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.factory.AddonsFactory;
import me.reb4ck.smp.api.hooks.EconomyPlugin;
import me.reb4ck.smp.api.hooks.PlaceholdersPlugin;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import me.reb4ck.smp.api.service.AddonsService;
import org.bukkit.entity.Player;

@Singleton
public final class AddonsServiceImpl implements AddonsService {
    private final PermissionsPlugin<Player> permissionsPlugin;
    private final PlaceholdersPlugin placeholdersPlugin;
    private final EconomyPlugin economyPlugin;

    @Inject
    public AddonsServiceImpl(AddonsFactory addonsFactory) {
        this.permissionsPlugin = addonsFactory.getPermissions();
        this.placeholdersPlugin = addonsFactory.getPlaceholders();
        this.economyPlugin = addonsFactory.getEconomy();
    }

    @Override
    public PermissionsPlugin<Player> getPermissions() {
        return permissionsPlugin;
    }

    @Override
    public PlaceholdersPlugin getPlaceholders() {
        return placeholdersPlugin;
    }

    @Override
    public EconomyPlugin getEconomy() {
        return economyPlugin;
    }
}
