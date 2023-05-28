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

public interface PermissionGetter {
    default int getServerSlots(Player player, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        UUID uuid = UUID.randomUUID();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.SERVER_SLOTS)
                .owner(player.getName())
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        try {
            return Integer.parseInt(future.get(3, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException e) {
            // Error while waiting for data.
            Console.sendMessage("Error while waiting for data 1 slot");
            return 1;
        } catch (TimeoutException e) {
            Console.sendMessage("Error while waiting for data 2 slot");
            return 1;
        }catch (NumberFormatException e){
            return 1;
        } finally {
            ReactiveServiceImpl.futures.remove(futureMessage.getUuid());
        }
    }

    default int getFavoriteSlots(Player player, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        UUID uuid = UUID.randomUUID();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.FAVORITE_SLOTS)
                .owner(player.getName())
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        try {
            return Integer.parseInt(future.get(3, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException e) {
            // Error while waiting for data.
            Console.sendMessage("Error while waiting for data 1 fav");
            return 1;
        } catch (TimeoutException e) {
            Console.sendMessage("Error while waiting for data 2 fav");
            return 1;
        }catch (NumberFormatException e){
            return 1;
        } finally {
            ReactiveServiceImpl.futures.remove(futureMessage.getUuid());
        }
    }

    default CompletableFuture<String> getFutureFavoriteSlots(Player player, BoxUtil util){
        CompletableFuture<String> future = new CompletableFuture<>();

        UUID uuid = UUID.randomUUID();

        ReactiveServiceImpl.futures.put(uuid, future);

        FutureMessage futureMessage = FutureMessage.builder()
                .futureType(FutureMessage.FutureType.FAVORITE_SLOTS)
                .owner(player.getName())
                .uuid(uuid)
                .build();

        util.redisService.publish(Channel.SMP_FUTURES_REQUESTS.getName(), util.persist.toString(futureMessage, Persist.PersistType.JSON));

        return future;
        /*try {
            return Integer.parseInt(future.get(3, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException e) {
            // Error while waiting for data.
            Console.sendMessage("Error while waiting for data 1 fav");
            return 1;
        } catch (TimeoutException e) {
            Console.sendMessage("Error while waiting for data 2 fav");
            return 1;
        }catch (NumberFormatException e){
            return 1;
        } finally {
            ReactiveServiceImpl.futures.remove(futureMessage.getUuid());
        }*/
    }
}
