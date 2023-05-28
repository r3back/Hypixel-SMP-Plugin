package me.reb4ck.smp.gui.admin_all;

import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.gui.FutureGUI;
import me.reb4ck.smp.gui.GUI;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.server.SMPServer;
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

public final class AdminAllServersGUI extends GUI implements SMPGetter {
    private final Map<Integer, String> servers = new HashMap<>();
    private final AdminAllServersGUIConfig config;
    private final String playerName;
    private final int playerSlots;
    private String trackId;

    public AdminAllServersGUI(BoxUtil util, String playerName, int playerSlots) {
        super(util.config.inventories().adminAllServersGUIConfig, util);

        this.playerName = playerName;
        this.playerSlots = playerSlots;
        this.config = configManager.inventories().adminAllServersGUIConfig;
    }

    @Override
    public @NotNull CompletableFuture<Inventory> getFutureInventory() {
        util.scheduler.runTaskAsynchronously(util.plugin, () -> {
            InventoryUtils.fillInventory(inventory, config.background);

            trackId = util.smpRestAPI.getTrackId(playerName).getValue();

            //
            int i = 0;

            List<SMPServer> serverList = getAllPlayerServers(trackId, util);

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

            setItem(config.closeGUI);


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
            player.closeInventory();
            player.performCommand("smp manage "+ servers.get(slot));
        }
    }
}
