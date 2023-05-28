package me.reb4ck.smp.gui;

import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.placeholder.IPlaceholder;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.utils.item.Item;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.placeholder.Placeholder;
import me.reb4ck.smp.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class GUI implements InventoryHolder, ClickableInventory{
    protected final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    protected final CompletableFuture<Inventory> future = new CompletableFuture<>();
    protected final Inventory inventory;
    protected final BoxUtil util;

    public GUI(int size, String title, BoxUtil util) {
        this.inventory = Bukkit.createInventory(this, size, StringUtils.color(title));
        this.configManager = util.config;
        this.util = util;
    }

    public GUI(SimpleGUI simpleGUI, BoxUtil util) {
        this.inventory = Bukkit.createInventory(this, simpleGUI.size, StringUtils.color(simpleGUI.title));
        this.configManager = util.config;
        this.util = util;
    }

    protected @NotNull CompletableFuture<Inventory> getFutureInventory() {
        return future;
    }

    public void openFuture(Player player){
        getFutureInventory().thenAccept(inv -> util.scheduler.runTask(util.plugin, () -> player.openInventory(inv)));
    }

    public void openSync(Player player){
        util.scheduler.runTask(util.plugin, () -> player.openInventory(getInventory()));
    }

    @Override
    public boolean isClickingCreatedGUI(InventoryClickEvent e){
        if(e.getClickedInventory() != null)
            return e.getClickedInventory().equals(inventory);
        else
            return false;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isClickingDecoration(Integer clickedSlot, Background background){
        return background.items.keySet().stream().anyMatch(slot -> slot.equals(clickedSlot));
    }

    @Override
    public void setItem(Item item) {
        if(!item.enabled) return;

        if(item.slot == null) return;

        inventory.setItem(item.slot, InventoryUtils.makeItem(item));
    }

    @Override
    public void setItem(Item item, List<IPlaceholder> placeholderList) {
        if(!item.enabled) return;

        if(item.slot == null) return;

        inventory.setItem(item.slot, InventoryUtils.makeItem(item, placeholderList));
    }

    protected boolean isItem(int slot, Item item){
        return item.enabled && item.slot == slot;
    }

    public void onInventoryClose(InventoryCloseEvent event){}

    public void onInventoryDrag(InventoryDragEvent event){}

    protected List<IPlaceholder> getInfo(SerializedGUI serializedGUI){
        boolean status = serializedGUI.getBoolean(SerializedGUI.Value.SERVER_STATUS);

        String message = configManager.messages().serverStatusPlaceholder.get(status);

        String smpName = serializedGUI.getString(SerializedGUI.Value.SERVER_NAME);

        String smpDisplay = serializedGUI.getString(SerializedGUI.Value.SERVER_DISPLAY_NAME);

        int smpCurrentPlayers = serializedGUI.getInteger(SerializedGUI.Value.SERVER_CURRENT_PLAYERS);

        int smpMaxPlayers = serializedGUI.getInteger(SerializedGUI.Value.SERVER_MAX_PLAYERS);

        int smpRam = serializedGUI.getInteger(SerializedGUI.Value.SERVER_RAM);

        String user = serializedGUI.getString(SerializedGUI.Value.SERVER_USER);

        String password = serializedGUI.getString(SerializedGUI.Value.SERVER_PASSWORD);

        Optional<SMPServer> server = util.smpService.getServers().stream().filter(smpServer -> smpServer.getName().equals(smpName)).findFirst();

        String timerFormat = configManager.messages().noFeatured;

        if(server.isPresent()){
            long remainingTime = ServerUtils.getRemainingTime(server.get());

            if(remainingTime > 0)
                timerFormat = StringUtils.getTimedMessage(configManager.messages().timeFormat, remainingTime, configManager);
        }

        return Arrays.asList(
                new Placeholder("server_status", message),
                new Placeholder("server_status", message),
                new Placeholder("server_name", smpName),
                new Placeholder("time_format", timerFormat),
                new Placeholder("server_user", user),
                new Placeholder("server_password", password),
                new Placeholder("server_ram", smpRam),

                new Placeholder("server_display_name", smpDisplay),
                new Placeholder("server_players", smpCurrentPlayers),
                new Placeholder("server_max_players", smpMaxPlayers)
        );
    }
}
