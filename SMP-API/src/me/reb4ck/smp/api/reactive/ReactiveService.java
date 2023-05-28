package me.reb4ck.smp.api.reactive;

import me.reb4ck.smp.api.Timer;
import me.reb4ck.smp.api.config.ConfigFeature;
import me.reb4ck.smp.api.ram.RamSettings;
import me.reb4ck.smp.server.SMPServer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ReactiveService {
    ConfigFeature getFeatureTimer();

    Timer getShutDownTimer();

    boolean isUnlimitedServer();

    int serversPerPage();

    RamSettings getRamPrices();

    boolean isCompleted();
}
