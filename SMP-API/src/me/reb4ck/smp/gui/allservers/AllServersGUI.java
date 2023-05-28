package me.reb4ck.smp.gui.allservers;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.gui.GUI;
import me.reb4ck.smp.gui.main.FutureMainMenuGUI;
import me.reb4ck.smp.gui.main.LobbyMainMenuGUI;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.message.FavoriteMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.utils.ServerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class AllServersGUI extends GUI implements SMPGetter {
    private final Map<Integer, SMPServer> recipeSlots = new HashMap<>();
    private final SerializedGUI serializedGUI;
    private final AllServersGUIConfig config;
    private final SMPFavorites smpFavorites;
    private boolean hasNext = false;
    private final boolean openSync;
    private final int playerSlots;
    private final String trackId;
    private final double money;
    private final int page;

    public AllServersGUI(BoxUtil util, int page, int playerSlots, SMPFavorites smpFavorites, double money, SerializedGUI serializedGUI, String trackId, boolean openSync) {
        super(util.config.inventories().allServersGUIConfig, util);

        this.config = configManager.inventories().allServersGUIConfig;
        this.serializedGUI = serializedGUI;
        this.smpFavorites = smpFavorites;
        this.playerSlots = playerSlots;
        this.openSync = openSync;
        this.trackId = trackId;
        this.money = money;
        this.page = page;
    }

    @Override
    public @NotNull CompletableFuture<Inventory> getFutureInventory() {
        util.scheduler.runTaskAsynchronously(util.plugin, () -> {
            InventoryUtils.fillInventory(inventory, config.background);

            List<SMPServer> servers = getAllServers(UUID.randomUUID(), util).stream()
                    .filter(server -> server.isEnabled() || server.getUuid().equals(trackId))
                    .collect(Collectors.toList());

            int maxPerPage = serializedGUI.getInteger(SerializedGUI.Value.SERVERS_PER_PAGE);

            int slot = 0;
            int i = maxPerPage * (page - 1);
            if(servers.size() > 0){
                while (slot < maxPerPage) {
                    if (servers.size() > i && i >= 0) {
                        SMPServer server = servers.get(i);
                        inventory.setItem(slot, InventoryUtils.makeItem(config.serverItem, ServerUtils.getPlaceholders(server), server));
                        recipeSlots.put(slot, server);
                        slot++;
                        i++;
                        continue;
                    }
                    slot++;
                }
            }
            hasNext = servers.size() > maxPerPage * page;

            if (this.page > 1)
                inventory.setItem(config.backPage.slot, InventoryUtils.makeItem(config.backPage));

            if (hasNext)
                inventory.setItem(config.nextPage.slot, InventoryUtils.makeItem(config.nextPage));

            setItem(config.goBack);

            future.complete(inventory);
        });

        return future;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if(!isClickingCreatedGUI(event)) return;

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if (isItem(slot, config.closeGUI)) {
            player.closeInventory();
        } else if (isItem(slot, config.goBack)) {
            if(openSync){
               player.openInventory(new LobbyMainMenuGUI(util, player.getName(), serializedGUI, true).getInventory());
            }else{
                player.closeInventory();
                new FutureMainMenuGUI(util, player.getName(), serializedGUI, false).openFuture(player);
            }
        }else if(isItem(slot, config.backPage) && page > 1){
            player.closeInventory();
            new AllServersGUI(util, page - 1, playerSlots, smpFavorites, money, serializedGUI, trackId, openSync).openFuture(player);
        }else if(isItem(slot, config.nextPage) && hasNext){
            player.closeInventory();
            new AllServersGUI(util, page + 1, playerSlots, smpFavorites, money, serializedGUI, trackId, openSync).openFuture(player);
        } else if (recipeSlots.containsKey(slot)) {
            SMPServer server = recipeSlots.get(slot);
            if (event.getClick().isRightClick()) {

                if (smpFavorites == null) return;

                List<String> favorites = smpFavorites.getFavorites();

                if (favorites.contains(server.getName())) {
                    player.closeInventory();
                    player.sendMessage(StringUtils.color(configManager.messages().alreadyFavorite));
                }else {
                    if(favorites.size() >= configManager.config().maxFavorites) {
                        player.sendMessage(StringUtils.color(configManager.messages().moreFavoritesAreNotAllowed));
                    }else if (favorites.size() >= playerSlots) {
                        player.sendMessage(StringUtils.color(configManager.messages().youDontHavePermissionsToAddAFavoriteServer));
                    } else {
                        player.sendMessage(StringUtils.color(configManager.messages().favoriteServerAdded.replace("%server%", server.getName())));

                        favorites.add(server.getName());

                        FavoriteMessage favoriteMessage = FavoriteMessage
                                .builder()
                                .actionType(FavoriteMessage.ActionType.ADD)
                                .server(server.getName())
                                .name(player.getName())
                                .build();

                        String message = util.persist.toString(favoriteMessage, Persist.PersistType.JSON);

                        util.redisService.publish(Channel.BUKKIT_TO_BUNGEE_FAVORITES.getName(), message);
                        return;
                    }
                    player.closeInventory();
                }
            } else if (event.isLeftClick()) {
                player.closeInventory();

                util.tpService.teleport(player, server, 0);

            }

        }
    }
}
