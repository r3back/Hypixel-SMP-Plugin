package me.reb4ck.smp.gui.main;

import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.server.SMPServerList;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.utils.ServerUtils;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public final class FutureMainMenuGUI extends AbstractMainMenuGUI{
    public FutureMainMenuGUI(BoxUtil util, String playerName, SerializedGUI serializedGUI, boolean openSync) {
        super(util, playerName, serializedGUI, openSync);
    }

    @Override
    public @NotNull CompletableFuture<Inventory> getFutureInventory() {
        util.scheduler.runTaskAsynchronously(util.plugin, () -> {
            try {
                CompletableFuture<String> serversFuture = getFutureAllServers(UUID.randomUUID(), util);

                CompletableFuture<String> favoritesFuture = getFutureFavorites(playerName, util);

                CompletableFuture.allOf(serversFuture, favoritesFuture);

                List<SMPServer> allServers = util.persist.load(SMPServerList.class, serversFuture.get(), Persist.PersistType.JSON).getServerList();

                InventoryUtils.fillInventory(inventory, config.background);

                setItem(config.allServers);

                setItem(config.joinRandomServer);

                List<SMPServer> featured = allServers.stream()
                        .filter(server -> ServerUtils.getRemainingTime(server) > 0)
                        .collect(Collectors.toList());

                int count = 0;
                for(Integer slot : config.featuredSlots) {

                    try {
                        SMPServer server = featured.get(count);

                        inventory.setItem(slot, InventoryUtils.makeItem(config.featured, ServerUtils.getPlaceholders(server), server));

                        featuredMap.put(slot, server.getName());

                    }catch (Exception e){
                        inventory.setItem(slot, InventoryUtils.makeItem(config.featuredEmpty));
                        featuredAvailable.add(slot);
                    }

                    count++;
                }

                smpFavorites = util.persist.load(SMPFavorites.class, favoritesFuture.get(), Persist.PersistType.JSON);

                if(smpFavorites != null){
                    List<String> favorites = smpFavorites.getFavorites();

                    int i = 0;
                    for(Integer slot : config.favoriteSlots){
                        if(i < favoriteSlots){
                            try {
                                String server = favorites.get(i);

                                Optional<SMPServer> smpServer = allServers.stream().filter(smpServer1 -> smpServer1.getName().equals(server)).findFirst();

                                if(smpServer.isPresent())
                                    inventory.setItem(slot, InventoryUtils.makeItem(config.fillFavoriteServer, ServerUtils.getPlaceholders(smpServer.get()), smpServer.get()));
                                else
                                    inventory.setItem(slot, InventoryUtils.makeItem(config.notFoundServer));

                                favoritesMap.put(slot, server);

                            }catch (Exception e){
                                inventory.setItem(slot, InventoryUtils.makeItem(config.emptyFavoriteServer));
                            }
                        }else{
                            inventory.setItem(slot, InventoryUtils.makeItem(config.lockedFavoriteServer));
                        }
                        i++;
                    }
                }

                setItem(config.createServer);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            future.complete(inventory);
        });

        return future;
    }
}
