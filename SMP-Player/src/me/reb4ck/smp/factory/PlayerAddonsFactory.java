package me.reb4ck.smp.factory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.reb4ck.smp.api.factory.AddonsFactory;
import me.reb4ck.smp.api.hooks.EconomyPlugin;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import me.reb4ck.smp.api.hooks.PlaceholdersPlugin;
import me.reb4ck.smp.api.hooks.PluginRecognizer;
import me.reb4ck.smp.base.hooks.PlaceholderAPIManager;
import org.bukkit.entity.Player;

public class PlayerAddonsFactory extends PluginRecognizer implements AddonsFactory {
    private final Injector injector;
    private final EconomyPlugin economyPlugin;
    private final PlaceholdersPlugin placeholdersPlugin;
    private final PermissionsPlugin<Player> permissionsPlugin;

    @Inject
    public PlayerAddonsFactory(Injector injector) {
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
        return null;
    }

    private PlaceholdersPlugin setupPlaceholders(){
        if(isPlugin("PlaceholderAPI"))
            return injector.getInstance(PlaceholderAPIManager.class);
        return null;
    }
    private EconomyPlugin setupEco(){
        return null;
    }

}