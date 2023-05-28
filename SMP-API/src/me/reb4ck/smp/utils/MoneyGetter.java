package me.reb4ck.smp.utils;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.reactive.ReactiveServiceImpl;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.FutureMessage;
import me.reb4ck.smp.persist.Persist;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface MoneyGetter {
    default double getMoney(Player player, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        UUID uuid = UUID.randomUUID();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.MONEY)
                .owner(player.getName())
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        try {
            return Double.parseDouble(future.get(3, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException e) {
            // Error while waiting for data.
            Console.sendMessage("Error while waiting for data 1");
            return 0D;
        } catch (TimeoutException e) {
            Console.sendMessage("Error while waiting for data 2");
            return 0D;
        }catch (NumberFormatException e){
            return 0D;
        } finally {
            ReactiveServiceImpl.futures.remove(futureMessage.getUuid());
        }
    }

    default CompletableFuture<String> getFutureMoney(Player player, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        UUID uuid = UUID.randomUUID();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.MONEY)
                .owner(player.getName())
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        return future;
    }
}
