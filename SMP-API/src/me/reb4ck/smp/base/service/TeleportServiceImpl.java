package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.exception.server.AlreadyOnlineException;
import me.reb4ck.smp.api.exception.server.OnlineLimitException;
import me.reb4ck.smp.api.service.ProxyService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.api.tracker.SMPRestAPI;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.TeleportRequest;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.api.service.TeleportService;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.CompletableFuture;

@Singleton
public final class TeleportServiceImpl implements TeleportService {

    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    private final BukkitScheduler taskScheduler;
    private final ProxyService proxyService;
    private final RedisService redisService;
    private final SMPService smpService;
    private final SMPRestAPI smpRestAPI;
    private final JavaPlugin plugin;
    private final Persist persist;

    @Inject
    public TeleportServiceImpl(JavaPlugin plugin, SMPService smpService, BukkitScheduler taskScheduler, ProxyService proxyService, Persist persist, RedisService redisService,
                               ConfigManager<Configuration, Messages, Commands, Inventories> configManager, SMPRestAPI smpRestAPI) {
        this.plugin = plugin;
        this.persist = persist;
        this.smpRestAPI = smpRestAPI;
        this.smpService = smpService;
        this.redisService = redisService;
        this.proxyService = proxyService;
        this.taskScheduler = taskScheduler;
        this.configManager = configManager;
    }


    @Override
    public CompletableFuture<Void> teleport(Player player, SMPServer smpServer, int delayInSeconds) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if(delayInSeconds >= 0)
            taskScheduler.runTaskLaterAsynchronously(plugin, () -> {
                try {
                    teleport(player, smpServer);
                } catch (AlreadyOnlineException | OnlineLimitException e) {
                    e.printStackTrace();
                }
            }, delayInSeconds * 20L);

        return future;
    }


    private void teleport(Player player, SMPServer smpServer) throws AlreadyOnlineException, OnlineLimitException {

        boolean isOnline = smpServer.isEnabled();

        ITrackID trackID = smpRestAPI.getTrackId(player.getName());

        if(!isOnline){
            if(trackID.getValue().equals(smpServer.getUuid())){

                player.sendMessage(StringUtils.color(configManager.messages().serverOffLineStarting));

                smpService.startServer(smpServer);

                teleport(player, smpServer, 10);

            }else{
                player.sendMessage(StringUtils.color(configManager.messages().cantTeleportIsOffline));
            }
        }else{
            tp(player, smpServer);
        }
    }

    @Override
    public void teleport(Player player, String server) {
        if(server == null) return;

        TeleportRequest teleportRequest = TeleportRequest.builder()
                .player(player.getName())
                .server(server)
                .type(TeleportRequest.Type.SERVER)
                .build();

        redisService.publish(Channel.TELEPORT_REQUESTS.getName(), persist.toString(teleportRequest, Persist.PersistType.JSON));

    }

    @Override
    public CompletableFuture<Void> teleportAllPlayers(SMPServer smpServer, String server) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        TeleportRequest teleportRequest = TeleportRequest.builder()
                .type(TeleportRequest.Type.ALL)
                .server(smpServer.getName())
                .build();

        redisService.publish(Channel.TELEPORT_REQUESTS.getName(), persist.toString(teleportRequest, Persist.PersistType.JSON));

        taskScheduler.runTaskLaterAsynchronously(plugin, () -> future.complete(null), 20);

        return future;
    }


    private void tp(Player player, SMPServer smpServer) {

        if(smpServer == null) return;

        TeleportRequest teleportRequest = TeleportRequest.builder()
                .player(player.getName())
                .type(TeleportRequest.Type.SMP)
                .server(persist.toString(smpServer, Persist.PersistType.JSON))
                .build();

        redisService.publish(Channel.TELEPORT_REQUESTS.getName(), persist.toString(teleportRequest, Persist.PersistType.JSON));

    }

    protected  <T> T getObject(Class<T> clazz, String str){
        return str == null || str.equals("") ? null : persist.load(clazz, str, Persist.PersistType.JSON);
    }
}
