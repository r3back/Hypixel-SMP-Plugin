package me.reb4ck.smp.gui.admin;

import me.reb4ck.smp.api.exception.server.NotOnlineException;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.gui.GUI;
import me.reb4ck.smp.gui.confirm.ConfirmGUI;
import me.reb4ck.smp.gui.description.DescriptionGUI;
import me.reb4ck.smp.gui.ram.RamUpgradeGUI;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.placeholder.IPlaceholder;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.placeholder.Placeholder;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import me.reb4ck.smp.api.gui.SerializedGUI.Value;

import java.util.Collections;
import java.util.List;

public final class IndividualServerGUI extends GUI implements SMPGetter {
    private final IndividualServerGUIConfig config;
    private final SerializedGUI serializedGUI;
    private final double money;

    public IndividualServerGUI(SerializedGUI serializedGUI, BoxUtil util, double money) {

        super(util.config.inventories().individualServerGUIConfig, util);

        this.money = money;
        this.serializedGUI = serializedGUI;
        this.config = configManager.inventories().individualServerGUIConfig;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.background);

        setItem(config.deleteServer);
        setItem(config.stopServer, getStatus());
        setItem(config.startServer, getStatus());
        setItem(config.teleportServer);
        setItem(config.infoServer, getInfo(serializedGUI));
        setItem(config.backButton);
        setItem(config.closeGUI);
        setItem(config.wikiItem);
        setItem(config.descriptionItem);
        setItem(config.opCommand);

        setItem(config.ramUpgrade, getInfo(serializedGUI));

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if(!isClickingCreatedGUI(event)) return;

        int slot = event.getSlot();

        Player player = (Player) event.getWhoClicked();

        String smpName = serializedGUI.getStrings().get(Value.SERVER_NAME.getValue());

        if(smpName == null) return;

        if(isItem(slot, config.wikiItem)) {
            event.setCancelled(true);
        }else if(isItem(slot, config.deleteServer)) {

            player.openInventory(new ConfirmGUI(util, smpName).getInventory());
        }else if(isItem(slot, config.startServer)){
            player.closeInventory();

            player.performCommand("smp start " + smpName);

        }else if(isItem(slot, config.stopServer)){
            player.closeInventory();

            player.performCommand("smp stop " + smpName);
        }else if(isItem(slot, config.backButton)){
            player.closeInventory();

            player.performCommand("smp admin");
        }else if(isItem(slot, config.teleportServer)) {
            player.closeInventory();

            util.scheduler.runTaskAsynchronously(util.plugin, () -> {
                SMPServer server = getServer(player, smpName, util);

                if(server == null) return;

                util.tpService.teleport(player, server, 0);
            });
        }else if(config.descriptionItem != null && isItem(slot, config.descriptionItem)){
            player.closeInventory();
            new DescriptionGUI(util, smpName).openFuture(player);
        }else if(isItem(slot, config.closeGUI)) {
            player.closeInventory();
        }else if(isItem(slot, config.ramUpgrade)){
            player.openInventory(new RamUpgradeGUI(util, serializedGUI, money).getInventory());
        }else if(isItem(slot, config.opCommand)){
            player.closeInventory();

            final String name = player.getName();

            util.scheduler.runTaskAsynchronously(util.plugin, () -> {
                SMPServer server = getServer(player, smpName, util);

                if(server == null) return;

                try {
                    util.smpService.sendCommand(server, "op " + name);

                    player.sendMessage(StringUtils.color(configManager.messages().successfullyOped.replace("%server%", server.getName())));

                }catch (NotOnlineException ignored){
                    player.sendMessage(StringUtils.color(configManager.messages().serverIsOff));
                }
            });
        }

    }

    private List<IPlaceholder> getStatus(){
        boolean status = serializedGUI.getBooleans().get(Value.SERVER_STATUS.getValue());

        String message = configManager.messages().serverStatusPlaceholder.get(status);

        return Collections.singletonList(new Placeholder("server_status", message));
    }

}
