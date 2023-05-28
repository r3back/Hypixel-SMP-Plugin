package me.reb4ck.smp.factory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.factory.AddonsFactory;
import me.reb4ck.smp.api.hooks.EconomyPlugin;
import me.reb4ck.smp.api.hooks.PlaceholdersPlugin;
import me.reb4ck.smp.api.hooks.PluginRecognizer;
import me.reb4ck.smp.base.hooks.*;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import org.bukkit.entity.Player;

public final class AddonsFactoryImpl extends PluginRecognizer implements AddonsFactory {
    private final Injector injector;
    private final EconomyPlugin economyPlugin;
    private final PlaceholdersPlugin placeholdersPlugin;
    private final PermissionsPlugin<Player> permissionsPlugin;

    @Inject
    public AddonsFactoryImpl(Injector injector) {
        this.injector = injector;
        this.permissionsPlugin = setupPerms();
        this.placeholdersPlugin = setupPlaceholders();
        this.economyPlugin = setupEco();
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


    private PermissionsPlugin<Player> setupPerms(){
        if(isPlugin("LuckPerms"))
            return injector.getInstance(LuckPermsPermissions.class);
        return injector.getInstance(InternalPermissions.class);
    }

    private PlaceholdersPlugin setupPlaceholders(){
        if(isPlugin("PlaceholderAPI"))
            return injector.getInstance(PlaceholderAPIManager.class);
        return null;
    }
    private EconomyPlugin setupEco(){
        if(isPlugin("TokenManager"))
            return injector.getInstance(EconomyTokenManager.class);
        return null;
    }

}
