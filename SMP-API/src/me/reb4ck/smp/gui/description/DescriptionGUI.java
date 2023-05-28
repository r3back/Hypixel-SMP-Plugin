package me.reb4ck.smp.gui.description;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.gui.GUI;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.SMPGetter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class DescriptionGUI extends GUI implements SMPGetter {
    private final DescriptionGUIConfig config;
    private final String name;

    public DescriptionGUI(BoxUtil util, String name) {
        super(util.config.inventories().descriptionGUIConfig, util);

        this.config = configManager.inventories().descriptionGUIConfig;
        this.name = name;
    }

    @Override
    public @NotNull CompletableFuture<Inventory> getFutureInventory() {
        util.scheduler.runTaskAsynchronously(util.plugin, () -> {
            InventoryUtils.fillInventory(inventory, config.background);

            SMPServer smpServer = getServer(UUID.randomUUID(), name, util);

            setItem(config.addLineItem);
            setItem(config.removeLineItem);
            setItem(config.resetDescriptionItem);

            if(smpServer != null) inventory.setItem(config.currentDescription.slot, InventoryUtils.makeItem(config.currentDescription, Collections.emptyList(), smpServer));

            setItem(config.goBack);
            setItem(config.closeGUI);

            future.complete(inventory);
        });


        return future;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getSlot();

        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if(!isClickingCreatedGUI(event)) return;

        if(isItem(slot, config.closeGUI)) {
            player.closeInventory();
        }else if(isItem(slot, config.goBack)){
            player.closeInventory();
            player.performCommand("smp manage " + name);
        }else if(isItem(slot, config.removeLineItem)){
            player.closeInventory();
            player.performCommand("smp removedescriptionline " + name);
        }else if(isItem(slot, config.addLineItem)){
            player.closeInventory();
            player.performCommand("smp adddescriptionline " + name);
        }else if(isItem(slot, config.resetDescriptionItem)){
            player.closeInventory();
            player.performCommand("smp resetdescription " + name);
        }
    }
}
