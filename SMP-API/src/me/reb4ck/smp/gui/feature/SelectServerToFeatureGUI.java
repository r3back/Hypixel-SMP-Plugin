package me.reb4ck.smp.gui.feature;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.gui.GUI;
import me.reb4ck.smp.gui.main.FutureMainMenuGUI;
import me.reb4ck.smp.gui.main.LobbyMainMenuGUI;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.ServerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class SelectServerToFeatureGUI extends GUI implements SMPGetter {
    private final Map<Integer, String> servers = new HashMap<>();
    private final SelectServerToFeatureGUIConfig config;
    private final SerializedGUI serializedGUI;
    private final SMPFavorites smpFavorites;
    private final String playerName;
    private final boolean openSync;
    private final int playerSlots;
    private final double money;
    private String trackId;

    public SelectServerToFeatureGUI(BoxUtil util, String playerName, int playerSlots, SMPFavorites smpFavorites, double money, SerializedGUI serializedGUI, boolean openSync) {
        super(util.config.inventories().selectServerToFeature, util);

        this.money = money;
        this.openSync = openSync;
        this.playerName = playerName;
        this.playerSlots = playerSlots;
        this.smpFavorites = smpFavorites;
        this.serializedGUI = serializedGUI;
        this.config = configManager.inventories().selectServerToFeature;
    }

    @Override
    public @NotNull CompletableFuture<Inventory> getFutureInventory() {
        InventoryUtils.fillInventory(inventory, config.background);

        util.scheduler.runTaskAsynchronously(util.plugin, () -> {
            trackId = util.smpRestAPI.getTrackId(playerName).getValue();

            List<SMPServer> serverList = getAllPlayerServers(trackId, util);
            //
            int i = 0;
            for(Integer slot : config.slots){
                if(i < playerSlots){
                    try {
                        SMPServer server = serverList.get(i);

                        inventory.setItem(slot, InventoryUtils.makeItem(config.displayItem, ServerUtils.getPlaceholders(server), server));

                        servers.put(slot, server.getName());

                    }catch (Exception e){
                        inventory.setItem(slot, InventoryUtils.makeItem(config.emptyItem));
                    }
                }else{
                    inventory.setItem(slot, InventoryUtils.makeItem(config.lockedItem));
                }
                i++;
            }
            //

            setItem(config.closeGUI);
            setItem(config.goBack);

            future.complete(inventory);
        });


        return future;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getSlot();

        event.setCancelled(true);

        if(!isClickingCreatedGUI(event)) return;

        Player player = (Player) event.getWhoClicked();

        if(isItem(slot, config.closeGUI)){
            player.closeInventory();
        }else if(servers.containsKey(slot)){
            player.openInventory(new FeatureServerGUI(util, playerSlots, servers.get(slot), smpFavorites, money, serializedGUI, trackId, openSync).getInventory());
        }else if(isItem(slot, config.goBack)){
            if(openSync){
                player.openInventory(new LobbyMainMenuGUI(util, player.getName(), serializedGUI, true).getInventory());
            }else{
                player.closeInventory();
                new FutureMainMenuGUI(util, player.getName(), serializedGUI, false).openFuture(player);
            }
        }
    }
}
