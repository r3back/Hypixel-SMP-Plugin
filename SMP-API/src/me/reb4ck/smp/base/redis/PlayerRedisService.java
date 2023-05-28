package me.reb4ck.smp.base.redis;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.handler.GUIHandler;
import me.reb4ck.smp.api.reactive.ReactiveService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.base.reactive.ReactiveServiceImpl;
import me.reb4ck.smp.base.server.SMPServerImpl;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.*;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisConfig;
import me.reb4ck.smp.redis.RedisPublisher;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.Console;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;

public final class PlayerRedisService implements RedisService {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    private StatefulRedisPubSubConnection<String, String> connection;
    private RedisPubSubAsyncCommands<String, String> async;
    private final RedisPublisher redisPublisher;
    private final BukkitScheduler scheduler;
    private final GUIHandler guiHandler;
    private final Injector injector;
    private final JavaPlugin plugin;
    private final Persist persist;
    private final Logger logger;
    private RedisClient client;
    private BoxUtil boxUtil;
    private String server;

    @Inject
    public PlayerRedisService(RedisConfig redisConfig, Persist persist, JavaPlugin plugin, BukkitScheduler scheduler, GUIHandler guiHandler, Injector injector,
                              RedisPublisher redisPublisher, ConfigManager<Configuration, Messages, Commands, Inventories> configManager) {
        this.plugin = plugin;
        this.persist = persist;
        this.scheduler = scheduler;
        this.injector = injector;
        this.guiHandler = guiHandler;
        this.logger = plugin.getLogger();
        this.configManager = configManager;
        this.redisPublisher = redisPublisher;
        this.server = getServer(plugin.getDataFolder());

        setupClient(redisConfig);
    }


    private void setupClient(RedisConfig redisConfig){
        try {
            /*if(configManager.config().useCredentialsFromHere)
                this.client = RedisClient.create(redisConfig.getUri());
            else*/
            this.client = RedisClient.create(configManager.config().privateRedisUri);

            connect();
        }catch (Exception ignored){
            logger.info("Failed Creating Redis Connection.");
        }

    }

    @Override
    public void connect() throws RedisConnectionException {
        try{

            logger.info("Trying to create Redis Connection.");

            this.connection = client.connectPubSub();

            this.async = connection.async();

            this.async.ping();

            logger.info("Successfully connected to redis!");

            trySubscribe();
        }catch (Exception ignored){
            logger.info("Failed Creating Redis Connection.");
        }
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

        redisPublisher.publishSync(Channel.SMP_UPDATES.getName(), updateMessage);
    }

    @Override
    public void publishSync(String channel, String message) {
        redisPublisher.publishSync(channel, message);
    }

    @Override
    public String getServer(File file) {
        return RedisService.super.getServer(file);
    }


    private void trySubscribe(){
        if(connection == null || async == null) return;

        logger.info("Successfully Create Subscriber side.");

        subscribe();
    }

    private void subscribe() {
        scheduler.runTaskAsynchronously(plugin, () -> {
            Bukkit.getConsoleSender().sendMessage("A");
            connection.addListener(new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String channel, String message) {
                    scheduler.runTaskAsynchronously(plugin, () -> {
                        if (channel.equals(Channel.COMMANDS.getName())) {
                            CommandMessage toExecute = getObject(CommandMessage.class, message);

                            if(toExecute == null) return;

                            if(!toExecute.getReceiver().equals(server)) return;

                            Bukkit.getConsoleSender().sendMessage("Executing " + toExecute.getCommand());

                            if (toExecute.getSender().equals(CommandMessage.Sender.CONSOLE))
                                scheduler.runTask(plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), toExecute.getCommand()));
                            else
                                Optional.ofNullable(Bukkit.getPlayer(toExecute.getSenderName())).ifPresent(player1 -> scheduler.runTask(plugin, () -> player1.performCommand(toExecute.getCommand())));
                        } else if (channel.equals(Channel.GUIS.getName())) {
                            GUIMessage toExecute = getObject(GUIMessage.class, message);

                            if(toExecute == null) return;

                            Player player = Bukkit.getPlayer(toExecute.getPlayer());

                            if (player == null) return;

                            guiHandler.open(player, toExecute, getBoxUtil());
                        } else if (channel.equals(Channel.SMP_UPDATES.getName())) {
                            SMPMessage smpMessage = getObject(SMPMessage.class, message);

                            if(smpMessage == null) return;

                            SMPServer server = getObject(SMPServerImpl.class, smpMessage.getSmpServer());

                            if(server == null) return;

                            getBoxUtil().smpService.updateServer(smpMessage.getActionType(), server);
                        } else if (channel.equals(Channel.SMP_FUTURES_ANSWERS.getName())) {
                            FutureMessage smpMessage = getObject(FutureMessage.class, message);

                            if(smpMessage == null) return;

                            if(ReactiveServiceImpl.futures.containsKey(smpMessage.getUuid()))
                                ReactiveServiceImpl.futures.get(smpMessage.getUuid()).complete(smpMessage.getReply());

                        }
                    });
                }
            });
            Bukkit.getConsoleSender().sendMessage("SUCCESSFULLY SUBSCRIBED!!");

            async.subscribe(Channel.SMP_FUTURES_ANSWERS.getName());
            Bukkit.getConsoleSender().sendMessage("SUCCESSFULLY SUBSCRIBED 2!!");

            async.subscribe(Channel.COMMANDS.getName());
            async.subscribe(Channel.GUIS.getName());
            Bukkit.getConsoleSender().sendMessage("SUCCESSFULLY SUBSCRIBED 2!!");

            async.subscribe(Channel.SMP_UPDATES.getName());
            Bukkit.getConsoleSender().sendMessage("SUCCESSFULLY SUBSCRIBED 23!!");

        });
    }

    private BoxUtil getBoxUtil(){
        if(boxUtil == null)
            this.boxUtil = injector.getInstance(BoxUtil.class);
        return boxUtil;
    }

    protected  <T> T getObject(Class<T> clazz, String str){
        return str == null || str.equals("") ? null : persist.load(clazz, str, Persist.PersistType.JSON);
    }
}
