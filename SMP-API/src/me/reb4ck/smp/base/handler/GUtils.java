package me.reb4ck.smp.base.handler;

import lombok.*;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.handler.GUIHandler;
import me.reb4ck.smp.api.reactive.ReactiveService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.api.service.AddonsService;
import me.reb4ck.smp.api.service.FavoriteService;
import me.reb4ck.smp.api.service.TeleportService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.database.Database;
import me.reb4ck.smp.api.tracker.SMPRestAPI;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class GUtils {
    public ConfigManager<Configuration, Messages, Commands, Inventories> config;
    public Persist.PersistType persistType = Persist.PersistType.JSON;
    public TeleportService tpService;
    public FavoriteService favoriteService;
    public RedisService redisService;
    public SMPService smpService;
    public SMPRestAPI smpRestAPI;
    public Persist persist;
    public GUIHandler guiHandler;
    public Database database;
    public BukkitScheduler scheduler;
    public JavaPlugin plugin;
    public AddonsService addonsService;
    public ReactiveService reactiveService;
}
