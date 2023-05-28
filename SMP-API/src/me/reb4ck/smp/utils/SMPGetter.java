package me.reb4ck.smp.utils;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.reactive.ReactiveServiceImpl;
import me.reb4ck.smp.base.server.SMPServerImpl;
import me.reb4ck.smp.base.server.SMPServerList;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.FutureMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface SMPGetter {
    default List<SMPServer> getAllPlayerServers(String trackId, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        UUID uuid = UUID.randomUUID();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.SMP_SERVER)
                .searchType(FutureMessage.SearchType.ALL_PLAYER)
                .owner(trackId)
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        try {
            return util.persist.load(SMPServerList.class, future.get(util.config.config().maxTimeoutTime, TimeUnit.SECONDS), Persist.PersistType.JSON).getServerList();
        } catch (InterruptedException | ExecutionException e) {
            // Error while waiting for data.
            Console.sendMessage("Error while waiting for data 1 allp");
            return null;
        } catch (TimeoutException e) {
            Console.sendMessage("Error while waiting for data 2 allp");
            return null;
        } finally {
            ReactiveServiceImpl.futures.remove(futureMessage.getUuid());
        }
    }

    default List<SMPServer> getAllServers(UUID uuid, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.SMP_SERVER)
                .searchType(FutureMessage.SearchType.ALL)
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        try {
            return util.persist.load(SMPServerList.class, future.get(util.config.config().maxTimeoutTime, TimeUnit.SECONDS), Persist.PersistType.JSON).getServerList();
        } catch (InterruptedException | ExecutionException e) {
            // Error while waiting for data.
            Console.sendMessage("Error while waiting for data 1 all");
            return null;
        } catch (TimeoutException e) {
            Console.sendMessage("Error while waiting for data 2 all");
            return null;
        } finally {
            ReactiveServiceImpl.futures.remove(futureMessage.getUuid());
        }
    }

    default CompletableFuture<String> getFutureAllServers(UUID uuid, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.SMP_SERVER)
                .searchType(FutureMessage.SearchType.ALL)
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        return future;
    }

    default SMPServer getServer(Player player, String name, String trackId, BoxUtil boxUtil){
        if(ReactiveServiceImpl.futures.containsKey(player.getUniqueId()))
            return null;

        return getServer(player.getUniqueId(), name, trackId, boxUtil);
    }

    default SMPServer getServer(UUID uuid, String name, String trackId, BoxUtil boxUtil){
        CompletableFuture<String> future = new CompletableFuture<>();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureFeature = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.SMP_SERVER)
                .uuid(uuid)
                .searchType(FutureMessage.SearchType.BY_OWNER_AND_NAME)
                .name(name)
                .owner(trackId)
                .build();

        return getServer(futureFeature, boxUtil.persist, boxUtil.redisService, boxUtil.config.config(), future);
    }

    default SMPServer getServer(Player player, String name, BoxUtil boxUtil){
        if(ReactiveServiceImpl.futures.containsKey(player.getUniqueId()))
            return null;

        return getServer(player.getUniqueId(), name, boxUtil);
    }

    default SMPServer getServer(UUID uuid, String name, BoxUtil boxUtil){
        CompletableFuture<String> future = new CompletableFuture<>();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureFeature = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.SMP_SERVER)
                .uuid(uuid)
                .searchType(FutureMessage.SearchType.BY_NAME)
                .name(name)
                .build();

        return getServer(futureFeature, boxUtil.persist, boxUtil.redisService, boxUtil.config.config(), future);
    }

    default SMPServer getServer(UUID uuid, String name, Persist persist, RedisService redisService, Configuration configuration){
        CompletableFuture<String> future = new CompletableFuture<>();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureFeature = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.SMP_SERVER)
                .uuid(uuid)
                .searchType(FutureMessage.SearchType.BY_NAME)
                .name(name)
                .build();

        return getServer(futureFeature, persist, redisService, configuration, future);
    }

    default SMPServer getServer(FutureMessage futureMessage, Persist persist, RedisService redisService, Configuration configuration, CompletableFuture<String> future){
        redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), persist.toString(futureMessage, Persist.PersistType.JSON));

        try {
            return persist.load(SMPServerImpl.class, future.get(configuration.maxTimeoutTime, TimeUnit.SECONDS), Persist.PersistType.JSON);
        } catch (InterruptedException | ExecutionException e) {
            // Error while waiting for data.
            Console.sendMessage("Error while waiting for data 1 player");
            return null;
        } catch (TimeoutException e) {
            Console.sendMessage("Error while waiting for data 2 player");
            return null;
        } finally {
            ReactiveServiceImpl.futures.remove(futureMessage.getUuid());
        }
    }
}
