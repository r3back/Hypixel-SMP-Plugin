package me.reb4ck.smp.gui.confirm;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.gui.GUI;
import me.reb4ck.smp.api.gui.SerializedGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class ConfirmGUI extends GUI {
    private final String smpServer;
    private final ConfirmGUIConfig config;

    public ConfirmGUI(BoxUtil util, String smpServer) {

        super(util.config.inventories().confirmDeleteGUI, util);

        this.smpServer = smpServer;
        this.config = util.config.inventories().confirmDeleteGUI;
    }

    @Override
    public @NotNull Inventory getInventory(){
        setItem(config.cancelItem);
        setItem(config.confirmItem);
        setItem(config.closeGUI);
        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getSlot();

        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if(!isClickingCreatedGUI(event)) return;

        if(isItem(slot, config.cancelItem) || isItem(slot, config.confirmItem) || isItem(slot, config.closeGUI)){
            player.closeInventory();
            if(isItem(slot, config.confirmItem))
                player.performCommand("smp delete "+ smpServer + " " + SerializedGUI.Value.DELETE_SERVER_CODE.getValue());

        }
    }
}
