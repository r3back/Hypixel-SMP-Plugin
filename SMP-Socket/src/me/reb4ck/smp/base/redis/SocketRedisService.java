package me.reb4ck.smp.base.redis;

import com.google.inject.Inject;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import me.reb4ck.smp.SocketSMP;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.handler.GUIHandler;
import me.reb4ck.smp.api.service.communicator.ReceiverCommunicatorService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.*;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisConfig;
import me.reb4ck.smp.redis.RedisPublisher;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import me.reb4ck.smp.message.SMPMessage.ActionType;

import java.util.Optional;
import java.util.logging.Logger;

public final class SocketRedisService implements RedisService {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    private StatefulRedisPubSubConnection<String, String> connection;
    private final ReceiverCommunicatorService communicatorService;
    private RedisPubSubAsyncCommands<String, String> async;
    private final RedisPublisher redisPublisher;
    private final BukkitScheduler scheduler;
    private final JavaPlugin plugin;
    private final Persist persist;
    private final String server;
    private final Logger logger;
    private RedisClient client;

    @Inject
    public SocketRedisService(RedisConfig redisConfig, Persist persist, JavaPlugin plugin, BukkitScheduler scheduler, ReceiverCommunicatorService communicatorService, RedisPublisher redisPublisher,
                              ConfigManager<Configuration, Messages, Commands, Inventories> configManager) {
        this.plugin = plugin;
        this.persist = persist;
        this.scheduler = scheduler;
        this.logger = plugin.getLogger();
        this.configManager = configManager;
        this.redisPublisher = redisPublisher;
        this.server = getServer(plugin.getDataFolder());
        this.communicatorService = communicatorService;

        setupClient(redisConfig);
    }


    private void setupClient(RedisConfig redisConfig){
        Bukkit.getScheduler().runTaskAsynchronously(SocketSMP.getInstance(), () -> {
            try {
            /*if(configManager.config().useCredentialsFromHere)
                this.client = RedisClient.create(redisConfig.getUri());
            else*/
                this.client = RedisClient.create(configManager.config().privateRedisUri);

                connect();
            }catch (Exception ignored){
                logger.info("Failed Creating Redis Connection.");
            }
        });


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
        publishUpdate(server, ActionType.UPDATE);
    }

    @Override
    public void publishUpdate(SMPServer server, ActionType actionType) {
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

    private void trySubscribe(){
        if(connection == null || async == null) return;

        logger.info("Successfully Create Subscriber side.");

        subscribe();
    }

    private void subscribe() {
        scheduler.runTaskAsynchronously(plugin, () -> {

            connection.addListener(new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String channel, String message) {
                    Bukkit.getConsoleSender().sendMessage("SUCCESSFULLY RECEIVED 2!!");


                    if(channel.equals(MessageReceiver.RECEIVER.getName())){
                        SMPMessage smpMessage = getObject(SMPMessage.class, message);

                        if(smpMessage == null) return;

                        communicatorService.handle(smpMessage);
                    }else if(channel.equals(MessageReceiver.TOKEN_MANAGER_RECEIVER.getName())) {
                        TMMessage toExecute = getObject(TMMessage.class, message);

                        if(toExecute == null) return;

                        String command = "tokenmanager remove " + toExecute.getName() + " " + ((int) toExecute.getAmount());

                        scheduler.runTask(plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command));
                    }
                }
            });
            Bukkit.getConsoleSender().sendMessage("SUCCESSFULLY SUBSCRIBED 1!!");

            if(isSocketReceiver()) {
                Bukkit.getConsoleSender().sendMessage("SUCCESSFULLY SUBSCRIBED 2!!");

                async.subscribe(MessageReceiver.RECEIVER.getName());
                async.subscribe(MessageReceiver.TOKEN_MANAGER_RECEIVER.getName());
                Bukkit.getConsoleSender().sendMessage("SUCCESSFULLY SUBSCRIBED 3!!");

            }
        });
    }

    private boolean isSocketReceiver(){
        return server.equals(MessageReceiver.RECEIVER.getName());
    }

    protected  <T> T getObject(Class<T> clazz, String str){
        return str == null || str.equals("") ? null : persist.load(clazz, str, Persist.PersistType.JSON);
    }
}
