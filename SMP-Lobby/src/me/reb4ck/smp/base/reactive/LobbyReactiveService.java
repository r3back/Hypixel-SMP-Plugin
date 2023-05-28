package me.reb4ck.smp.base.reactive;

import com.google.inject.Inject;
import me.reb4ck.smp.api.Timer;
import me.reb4ck.smp.api.config.ConfigFeature;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.ram.RamSettings;
import me.reb4ck.smp.api.reactive.ReactiveService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;

public final class LobbyReactiveService implements ReactiveService {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;

    @Inject
    public LobbyReactiveService(ConfigManager<Configuration, Messages, Commands, Inventories> configManager) {
        this.configManager = configManager;
    }

    @Override
    public ConfigFeature getFeatureTimer() {
        return configManager.config().configFeature;
    }

    @Override
    public Timer getShutDownTimer() {
        return configManager.config().shutdownUnlimitedServersAfter;
    }

    @Override
    public boolean isUnlimitedServer() {
        return false;
    }

    @Override
    public int serversPerPage() {
        return configManager.config().serverPerPages;
    }

    @Override
    public RamSettings getRamPrices() {
        return configManager.config().ramSettings;
    }


    @Override
    public boolean isCompleted() {
        return false;
    }
}
