package me.reb4ck.smp.gui.main;

import me.reb4ck.smp.api.service.FavoriteService;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.ServerUtils;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class LobbyMainMenuGUI extends AbstractMainMenuGUI {

    public LobbyMainMenuGUI(BoxUtil util, String playerName, SerializedGUI serializedGUI, boolean openSync) {
        super(util, playerName, serializedGUI, openSync);
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.background);

        List<SMPServer> allServers = util.smpService.getServers();

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

        smpFavorites = util.favoriteService.getFavorites(trackId, FavoriteService.ToSearch.UUID).orElse(null);

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

        return inventory;
    }
}
