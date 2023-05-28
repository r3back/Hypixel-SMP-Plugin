package me.reb4ck.smp.utils;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.reactive.ReactiveServiceImpl;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.FutureMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.message.FutureMessage.FutureType;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface FavoriteGetter {
    default SMPFavorites getFavorites(String playerName, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        UUID uuid = UUID.randomUUID();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureType.FAVORITE_NAMES)
                .owner(playerName)
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        try {
            String content = future.get(3, TimeUnit.SECONDS);

            return util.persist.load(SMPFavorites.class, content, Persist.PersistType.JSON);
        } catch (InterruptedException | ExecutionException e) {
            // Error while waiting for data.
            Console.sendMessage("Error while waiting for data 1 favn");
            return null;
        } catch (TimeoutException e) {
            Console.sendMessage("Error while waiting for data 2 favn");
            return null;
        } finally {
            ReactiveServiceImpl.futures.remove(futureMessage.getUuid());
        }
    }

    default CompletableFuture<String> getFutureFavorites(String playerName, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        UUID uuid = UUID.randomUUID();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureType.FAVORITE_NAMES)
                .owner(playerName)
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        return future;
    }
}
