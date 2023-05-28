package me.reb4ck.smp.base.redislobby;

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
import me.reb4ck.smp.api.service.AddonsService;
import me.reb4ck.smp.api.service.FavoriteService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.base.reactive.ReactiveServiceImpl;
import me.reb4ck.smp.base.server.SMPServerImpl;
import me.reb4ck.smp.base.server.SMPServerList;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.CommandMessage;
import me.reb4ck.smp.message.FutureMessage;
import me.reb4ck.smp.message.GUIMessage;
import me.reb4ck.smp.message.SMPMessage;
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
import java.util.stream.Collectors;

public final class LobbyRedisService implements RedisService {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    private StatefulRedisPubSubConnection<String, String> connection;
    private RedisPubSubAsyncCommands<String, String> async;
    private final RedisPublisher redisPublisher;
    private final BukkitScheduler scheduler;
    private final SMPService smpService;
    private final GUIHandler guiHandler;
    private final Injector injector;
    private final JavaPlugin plugin;
    private final Persist persist;
    private final Logger logger;
    private RedisClient client;
    private BoxUtil boxUtil;

    @Inject
    public LobbyRedisService(RedisConfig redisConfig, Persist persist, JavaPlugin plugin, BukkitScheduler scheduler, GUIHandler guiHandler, SMPService smpService, RedisPublisher redisPublisher,
                             ConfigManager<Configuration, Messages, Commands, Inventories> configManager, Injector injector) {
        this.plugin = plugin;
        this.persist = persist;
        this.scheduler = scheduler;
        this.smpService = smpService;
        this.guiHandler = guiHandler;
        this.logger = plugin.getLogger();
        this.injector = injector;
        this.configManager = configManager;
        this.redisPublisher = redisPublisher;
        getServer(plugin.getDataFolder());

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
            connection.addListener(new RedisPubSubAdapter<String, String>() {
                @Override
                public void message(String channel, String message) {
                    if (channel.equals(Channel.GUIS.getName())) {
                        GUIMessage toExecute = getObject(GUIMessage.class, message);

                        if (toExecute == null) return;

                        Player player = Bukkit.getPlayer(toExecute.getPlayer());

                        if (player == null) return;

                        guiHandler.open(player, toExecute, getBoxUtil());

                        return;
                    } else if (channel.equals(Channel.SMP_FUTURES_ANSWERS.getName())) {
                        FutureMessage smpMessage = getObject(FutureMessage.class, message);

                        if(smpMessage == null) return;

                        if(ReactiveServiceImpl.futures.containsKey(smpMessage.getUuid()))
                            ReactiveServiceImpl.futures.get(smpMessage.getUuid()).complete(smpMessage.getReply());

                    }

                    scheduler.runTaskAsynchronously(plugin, () -> {
                         if (channel.equals(Channel.SMP_UPDATES.getName())) {
                            SMPMessage smpMessage = getObject(SMPMessage.class, message);

                            if (smpMessage == null) return;

                            SMPServer server = getObject(SMPServerImpl.class, smpMessage.getSmpServer());

                            if (server == null) return;

                            Console.sendMessage("Received Update of server " + server.getName() + " is " + server.isEnabled());

                            smpService.updateServer(smpMessage.getActionType(), server);
                        } else if (channel.equals(Channel.SMP_FUTURES_REQUESTS.getName())) {
                             FutureMessage smpMessage = getObject(FutureMessage.class, message);

                             if (smpMessage == null) return;

                             if (smpMessage.getFutureType().equals(FutureMessage.FutureType.FEATURE_REQUEST))
                                 smpMessage.setReply(persist.toString(configManager.config().configFeature, Persist.PersistType.JSON));
                             else if (smpMessage.getFutureType().equals(FutureMessage.FutureType.SHUTDOWN_REQUEST))
                                 smpMessage.setReply(persist.toString(configManager.config().shutdownUnlimitedServersAfter, Persist.PersistType.JSON));
                             else if (smpMessage.getFutureType().equals(FutureMessage.FutureType.IS_UNLIMITED_REQUEST))
                                 smpMessage.setReply(booleanToString(getBoxUtil().addonsService.getPermissions().hasDefaultPermission(smpMessage.getReply(), configManager.config().unlimitedServerPermission)));
                             else if (smpMessage.getFutureType().equals(FutureMessage.FutureType.RAM_PRICES))
                                 smpMessage.setReply(persist.toString(configManager.config().ramSettings, Persist.PersistType.JSON));

                             else if (smpMessage.getFutureType().equals(FutureMessage.FutureType.FAVORITE_SLOTS)) {
                                 smpMessage.setReply(String.valueOf(getBoxUtil().addonsService.getPermissions().getFavoriteSlots(smpMessage.getOwner())));
                             } else if (smpMessage.getFutureType().equals(FutureMessage.FutureType.SERVER_SLOTS)) {
                                 smpMessage.setReply(String.valueOf(getBoxUtil().addonsService.getPermissions().getServerSlots(smpMessage.getOwner())));
                             } else if (smpMessage.getFutureType().equals(FutureMessage.FutureType.MONEY)) {
                                 smpMessage.setReply(String.valueOf(getBoxUtil().addonsService.getEconomy().getMoney(smpMessage.getOwner())));

                             } else if (smpMessage.getFutureType().equals(FutureMessage.FutureType.FAVORITE_NAMES)) {
                                 smpMessage.setReply(persist.toString(getBoxUtil().favoriteService.getFavorites(smpMessage.getOwner(), FavoriteService.ToSearch.NAME).orElse(null), Persist.PersistType.JSON));
                             } else if (smpMessage.getFutureType().equals(FutureMessage.FutureType.SMP_SERVER)) {
                                 switch (smpMessage.getSearchType()) {
                                     case ALL:
                                         smpMessage.setReply(persist.toString(new SMPServerList(smpService.getServers()), Persist.PersistType.JSON));
                                         break;
                                     case ALL_PLAYER:
                                         smpMessage.setReply(persist.toString(new SMPServerList(smpService.getServers().stream()
                                                 .filter(smp -> smp.getUuid().equals(smpMessage.getOwner()))
                                                 .collect(Collectors.toList())), Persist.PersistType.JSON));
                                         break;
                                     case BY_NAME:
                                         smpMessage.setReply(persist.toString(smpService.getServer(smpMessage.getName()).orElse(null), Persist.PersistType.JSON));
                                         break;
                                     case BY_OWNER:
                                         smpMessage.setReply(persist.toString(smpService.getServers().stream()
                                                 .filter(smpServer -> smpServer.getUuid().equals(smpMessage.getOwner())).findFirst().orElse(null), Persist.PersistType.JSON));
                                         break;
                                     case BY_OWNER_AND_NAME:
                                         smpMessage.setReply(persist.toString(smpService.getServer(smpMessage.getName())
                                                 .filter(smpServer -> smpServer.getUuid().equals(smpMessage.getOwner())).orElse(null), Persist.PersistType.JSON));
                                         break;
                                 }

                             } else
                                 return;

                             redisPublisher.publish(Channel.SMP_FUTURES_ANSWERS.getName(), persist.toString(smpMessage, Persist.PersistType.JSON));

                         }
                    });
                }
            });
            async.subscribe(Channel.SMP_FUTURES_REQUESTS.getName());
            async.subscribe(Channel.SMP_FUTURES_ANSWERS.getName());
            async.subscribe(Channel.SMP_UPDATES.getName());
            async.subscribe(Channel.GUIS.getName());
        });
    }

    private BoxUtil getBoxUtil(){
        if(boxUtil == null)
            this.boxUtil = injector.getInstance(BoxUtil.class);
        return boxUtil;
    }

    private String booleanToString(boolean value){
        return value ? "true" : "false";
    }

    protected  <T> T getObject(Class<T> clazz, String str){
        return str == null || str.equals("") ? null : persist.load(clazz, str, Persist.PersistType.JSON);
    }
}
