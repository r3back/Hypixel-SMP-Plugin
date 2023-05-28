package me.reb4ck.smp.api.utils;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.factory.DatabaseFactory;
import me.reb4ck.smp.api.handler.GUIHandler;
import me.reb4ck.smp.api.reactive.ReactiveService;
import me.reb4ck.smp.api.service.AddonsService;
import me.reb4ck.smp.api.service.FavoriteService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.service.TeleportService;
import me.reb4ck.smp.api.tracker.SMPRestAPI;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.database.Database;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisService;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class BoxUtil {
    public ConfigManager<Configuration, Messages, Commands, Inventories> config;
    public Persist.PersistType persistType = Persist.PersistType.JSON;
    public TeleportService tpService;
    public FavoriteService favoriteService;
    public RedisService redisService;
    public SMPService smpService;
    public SMPRestAPI smpRestAPI;
    public Persist persist;
    public GUIHandler guiHandler;
    public BukkitScheduler scheduler;
    public JavaPlugin plugin;
    public AddonsService addonsService;
    public ReactiveService reactiveService;

    @Inject
    public BoxUtil(ConfigManager<Configuration, Messages, Commands, Inventories> config, TeleportService tpService, FavoriteService favoriteService, RedisService redisService,
                   SMPService smpService, SMPRestAPI smpRestAPI, Persist persist, GUIHandler guiHandler, BukkitScheduler scheduler, JavaPlugin plugin, AddonsService addonsService,
                   ReactiveService reactiveService) {
        this.config = config;
        this.tpService = tpService;
        this.favoriteService = favoriteService;
        this.redisService = redisService;
        this.smpService = smpService;
        this.smpRestAPI = smpRestAPI;
        this.persist = persist;
        this.guiHandler = guiHandler;
        this.scheduler = scheduler;
        this.plugin = plugin;
        this.addonsService = addonsService;
        this.reactiveService = reactiveService;
    }
}
