package me.reb4ck.smp.base.redis;

import com.google.inject.Inject;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.pubsub.*;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import me.reb4ck.smp.api.exception.server.AlreadyOnlineException;
import me.reb4ck.smp.api.exception.server.OnlineLimitException;
import me.reb4ck.smp.api.service.TeleportService;
import me.reb4ck.smp.base.server.SMPServerImpl;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.*;
import me.reb4ck.smp.redis.*;
import me.reb4ck.smp.server.SMPServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.Optional;
import java.util.logging.Logger;

public final class BGRedisService implements RedisService {
    private StatefulRedisPubSubConnection<String, String> connection;
    private RedisPubSubAsyncCommands<String, String> async;
    private final TeleportService teleportService;
    private final RedisPublisher redisPublisher;
    private final TaskScheduler taskScheduler;
    private final RedisClient client;
    private final Persist persist;
    private final Logger logger;
    private final Plugin plugin;

    @Inject
    public BGRedisService(RedisConfig redisConfig, Plugin plugin, TaskScheduler scheduler, Persist persist, TeleportService teleportService, RedisPublisher redisPublisher) {
        this.plugin = plugin;
        this.persist = persist;
        this.taskScheduler = scheduler;
        this.logger = plugin.getLogger();
        this.redisPublisher = redisPublisher;
        this.teleportService = teleportService;

        this.client = RedisClient.create(redisConfig.getUri());

        connect();
    }

    @Override
    public void connect() throws RedisConnectionException {
        taskScheduler.runAsync(plugin, () -> {
            try {

                logger.info("Trying to create Redis Connection.");

                this.connection = client.connectPubSub();

                this.async = connection.async();

                this.async.ping();

                logger.info("Successfully connected to redis!");

                subscribe();

            } catch (Exception ignored) {
                logger.info("Failed Creating Redis Connection.");
            }
        });
    }

    @Override
    public void disable() {
        logger.info("Closing Redis Connection.");

        Optional.ofNullable(connection).ifPresent(StatefulConnection::closeAsync);

        Optional.ofNullable(client).ifPresent(AbstractRedisClient::shutdown);
    }

    @Override
    public void publish(String channel, String message) {
        redisPublisher.publish(channel, message);

    }

    @Override
    public void publish(String channel, Message message) {
        redisPublisher.publish(channel, message);
    }

    @Override
    public void publishUpdate(SMPServer server) {
        publishUpdate(server, SMPMessage.ActionType.UPDATE);
    }

    @Override
    public void publishUpdate(SMPServer server, SMPMessage.ActionType actionType) {
        String updateMessage = persist.toString(
                SMPMessage.builder()
                        .actionType(actionType)
                        .smpServer(persist.toString(server, Persist.PersistType.JSON))
                        .build(),
                Persist.PersistType.JSON);

        redisPublisher.publish(Channel.SMP_UPDATES.getName(), updateMessage);
    }

    @Override
    public void publishSync(String channel, String message) {
        redisPublisher.publishSync(channel, message);
    }

    private void subscribe() {
        taskScheduler.runAsync(plugin, () -> {
            connection.addListener(new Subscription());
            async.subscribe(Channel.TELEPORT_REQUESTS.getName());
            async.subscribe(Channel.REMOVE_FROM_BUNGEE.getName());
        });
    }

    private class Subscription extends RedisPubSubAdapter<String, String> {
            @Override
            public void message(String channel, String message) {



                if (channel.equals(Channel.TELEPORT_REQUESTS.getName())) {

                    TeleportRequest teleportRequest = getObject(TeleportRequest.class, message);

                    if (teleportRequest == null) return;

                    taskScheduler.runAsync(plugin, () -> {

                        try {

                            if(teleportRequest.getType().equals(TeleportRequest.Type.SMP)) {
                                SMPServer server = getObject(SMPServerImpl.class, teleportRequest.getServer());

                                if (server == null) return;

                                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(teleportRequest.getPlayer());

                                if (player == null) return;

                                teleportService.teleport(player, server);
                            }else if(teleportRequest.getType().equals(TeleportRequest.Type.ALL)){
                                ServerInfo serverInfo = ProxyServer.getInstance().getServers().getOrDefault(teleportRequest.getServer(), null);

                                if(serverInfo == null) return;

                                serverInfo.getPlayers().forEach(player -> teleportService.teleport(player, "lobby"));

                            }else{
                                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(teleportRequest.getPlayer());

                                if(player == null) return;

                                teleportService.teleport(player, teleportRequest.getServer());
                            }


                        } catch (Exception | AlreadyOnlineException | OnlineLimitException e) {
                            e.printStackTrace();
                        }
                    });
                }else if(channel.equals(Channel.REMOVE_FROM_BUNGEE.getName())){

                    if(message == null) return;

                    ProxyServer.getInstance().getServers().remove(message);
                }
            }
    }


    protected  <T> T getObject(Class<T> clazz, String str){
        return str == null || str.equals("") ? null : persist.load(clazz, str, Persist.PersistType.JSON);
    }
}