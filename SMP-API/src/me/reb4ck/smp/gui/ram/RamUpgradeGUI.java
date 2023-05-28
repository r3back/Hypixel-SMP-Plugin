package me.reb4ck.smp.gui.ram;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.gui.GUI;
import me.reb4ck.smp.gui.admin.IndividualServerGUI;
import me.reb4ck.smp.message.MessageReceiver;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.message.TMMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.api.placeholder.IPlaceholder;
import me.reb4ck.smp.base.placeholder.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import me.reb4ck.smp.api.gui.SerializedGUI.Value;

import java.util.*;

public final class RamUpgradeGUI extends GUI implements SMPGetter {
    private final Map<Integer, RamUpgrade> ramUpgradeMap = new HashMap<>();
    private final SerializedGUI serializedGUI;
    private final RamUpgradeGUIConfig config;
    private final double money;

    public RamUpgradeGUI(BoxUtil util, SerializedGUI serializedGUI, double money) {
        super(util.config.inventories().mainMenuGUIConfig, util);

        this.config = configManager.inventories().ramUpgradeGUIConfig;
        this.serializedGUI = serializedGUI;
        this.money = money;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.background);

        int smpRam = serializedGUI.getInteger(Value.SERVER_RAM);

        for(RamItem item : config.ramAndItems){
            RamUpgrade rmUpgrade = item.getRamUpgrade();

            int price = serializedGUI.getInteger(Value.SERVER_RAM_AMOUNT + String.valueOf(rmUpgrade.getRamAmount()));

            String canUpgrade = smpRam >= rmUpgrade.getRamAmount() ? configManager.messages().alreadyUpgraded : configManager.messages().clickToUpgrade;

            inventory.setItem(item.slot, InventoryUtils.makeItem(item, Arrays.asList(
                    new Placeholder("can_upgrade", canUpgrade),
                    new Placeholder("price", price)
            )));


            if(smpRam < rmUpgrade.getRamAmount()) {

                rmUpgrade.setRamPrice(price);

                ramUpgradeMap.put(item.slot, rmUpgrade);
            }
        }

        setItem(config.currentRamItem, getInfo(serializedGUI));
        setItem(config.closeGUI);
        setItem(config.goBack);

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if(!isClickingCreatedGUI(event)) return;

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if(isItem(slot, config.goBack)){
            player.openInventory(new IndividualServerGUI(serializedGUI, util, money).getInventory());
        }else if(isItem(slot, config.closeGUI)){
            player.closeInventory();
        }else if(ramUpgradeMap.containsKey(slot)){
            player.closeInventory();

            util.scheduler.runTaskAsynchronously(util.plugin, () -> {
                RamUpgrade upgrade = ramUpgradeMap.get(slot);

                double price = upgrade.getRamPrice();

                if(money < price){
                    player.sendMessage(StringUtils.color(configManager.messages().notEnoughMoneyToRam));
                    return;
                }

                int smpRam = serializedGUI.getInteger(Value.SERVER_RAM);

                if(smpRam >= upgrade.getRamAmount()) return;

                SMPServer server = getServer(player.getUniqueId(), serializedGUI.getString(Value.SERVER_NAME), util);

                if(server == null) return;

                List<IPlaceholder> placeholders = Arrays.asList(
                        new Placeholder("old_server_ram", smpRam),
                        new Placeholder("server_ram", upgrade.getRamAmount()),
                        new Placeholder("price", price));

                player.sendMessage(StringUtils.processMultiplePlaceholders(configManager.messages().successfullyUpdatedRam, placeholders));

                server.setRam(upgrade.getRamAmount());

                util.redisService.publishUpdate(server);

                String tmMessage = util.persist.toString(new TMMessage(player.getName(), price), Persist.PersistType.JSON);

                util.redisService.publish(MessageReceiver.TOKEN_MANAGER_RECEIVER.getName(), tmMessage);
            });

        }
    }
}
