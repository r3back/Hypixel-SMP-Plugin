package me.reb4ck.smp.gui;

import me.reb4ck.smp.utils.item.Item;
import me.reb4ck.smp.api.placeholder.IPlaceholder;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public interface ClickableInventory {
    void onInventoryClick(InventoryClickEvent event);
    void setItem(Item item);
    void setItem(Item item, List<IPlaceholder> placeholders);
    boolean isClickingDecoration(Integer clickedSlot, Background background);
    boolean isClickingCreatedGUI(InventoryClickEvent event);
}
