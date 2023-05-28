package me.reb4ck.smp.gui;

import org.bukkit.event.inventory.InventoryCloseEvent;

@FunctionalInterface
public interface ClosableInventory {
    void onInventoryClose(InventoryCloseEvent event);
}
