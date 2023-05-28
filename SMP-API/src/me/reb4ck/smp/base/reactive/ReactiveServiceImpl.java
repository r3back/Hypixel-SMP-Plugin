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
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.FutureMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.message.FutureMessage.FutureType;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.ServerGetter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class ReactiveServiceImpl implements ReactiveService, ServerGetter, SMPGetter {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    public static final Map<UUID, CompletableFuture<String>> futures = new HashMap<>();
    private final RedisService redisService;
    private final BukkitScheduler scheduler;
    private final JavaPlugin plugin;
    private final Persist persist;

    private Timer shutdownTimer;
    private Boolean isUnlimited;
    private ConfigFeature featureTimer;
    private RamSettings ramPrices;

    private final String server;

    @Inject
    public ReactiveServiceImpl(RedisService redisService, Persist persist, ConfigManager<Configuration, Messages, Commands, Inventories> configManager,
                               BukkitScheduler scheduler, JavaPlugin plugin) {
        this.server = getServerName(plugin);
        this.configManager = configManager;
        this.redisService = redisService;
        this.persist = persist;
        this.scheduler = scheduler;
        this.plugin = plugin;

        setup();
    }

    private void setup(){
        setupShutdownTimer();
        setupFeatureTimer();
        setupIsUnlimited();
        setupRam();
    }

    @Override
    public ConfigFeature getFeatureTimer() {
        return featureTimer;
    }

    @Override
    public Timer getShutDownTimer() {
        return shutdownTimer;
    }

    @Override
    public boolean isUnlimitedServer() {
        return isUnlimited;
    }

    @Override
    public int serversPerPage() {
        /**
         * TODO
         * Finish Stuff
         * to connect
         */
        return 43;
    }

    @Override
    public RamSettings getRamPrices() {
        return ramPrices;
    }

    @Override
    public boolean isCompleted() {
        return shutdownTimer != null && featureTimer != null && isUnlimited != null;
    }

    private void setupFeatureTimer(){
        UUID featureUUID = UUID.randomUUID();

        CompletableFuture<String> featureFuture = new CompletableFuture<>();

        futures.put(featureUUID, featureFuture);

        featureFuture.thenAccept(str -> {
            ConfigFeature timer = persist.load(ConfigFeature.class, str, Persist.PersistType.JSON);

            if(timer == null) return;

            this.featureTimer = timer;

            futures.remove(featureUUID);
        });

        //FutureType;
        FutureMessage futureFeature = FutureMessage.builder()
                .futureType(FutureType.FEATURE_REQUEST)
                .uuid(featureUUID)
                .build();

        redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), persist.toString(futureFeature, Persist.PersistType.JSON));
    }


    private void setupShutdownTimer(){
        UUID shutdownUUID = UUID.randomUUID();

        CompletableFuture<String> shutdownFuture = new CompletableFuture<>();

        futures.put(shutdownUUID, shutdownFuture);


        shutdownFuture.thenAccept(str -> {
            Timer timer = persist.load(Timer.class, str, Persist.PersistType.JSON);

            if(timer == null) return;

            this.shutdownTimer = timer;

            futures.remove(shutdownUUID);
        });


        FutureMessage futureShutdown = FutureMessage.builder()
                .futureType(FutureType.SHUTDOWN_REQUEST)
                .uuid(shutdownUUID)
                .build();

        redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), persist.toString(futureShutdown, Persist.PersistType.JSON));
    }

    private void setupIsUnlimited(){

        scheduler.runTaskAsynchronously(plugin, () -> {
            SMPServer server = getServer(UUID.randomUUID(), this.server, persist, redisService, configManager.config());

            if(server == null) return;

            CompletableFuture<String> future = new CompletableFuture<>();

            UUID uuid = UUID.randomUUID();

            futures.put(uuid, future);

            future.thenAccept(str -> {

                if(str == null) return;

                Boolean timer = Boolean.parseBoolean(str);

                if(timer == null) return;

                isUnlimited = timer;

                futures.remove(uuid);
            });

            FutureMessage futureShutdown = FutureMessage.builder()
                    .futureType(FutureType.IS_UNLIMITED_REQUEST)
                    .reply(server.getOwnerName())
                    .uuid(uuid)
                    .build();

            redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), persist.toString(futureShutdown, Persist.PersistType.JSON));
        });

    }

    private void setupRam(){
        UUID featureUUID = UUID.randomUUID();

        CompletableFuture<String> featureFuture = new CompletableFuture<>();

        futures.put(featureUUID, featureFuture);

        featureFuture.thenAccept(str -> {
            RamSettings timer = persist.load(RamSettings.class, str, Persist.PersistType.JSON);

            if(timer == null) return;

            this.ramPrices = timer;

            futures.remove(featureUUID);
        });

        //FutureType;
        FutureMessage futureFeature = FutureMessage.builder()
                .futureType(FutureType.RAM_PRICES)
                .uuid(featureUUID)
                .build();

        redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), persist.toString(futureFeature, Persist.PersistType.JSON));
    }
}
