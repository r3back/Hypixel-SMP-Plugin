package me.reb4ck.smp.gui.feature;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.gui.GUI;
import me.reb4ck.smp.message.MessageReceiver;
import me.reb4ck.smp.utils.InventoryUtils;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.message.TMMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.placeholder.Placeholder;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.api.config.ConfigFeature;
import me.reb4ck.smp.utils.ServerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class FeatureServerGUI extends GUI implements SMPGetter {
    private final FeatureServerGUIConfig config;
    private final SerializedGUI serializedGUI;
    private final SMPFavorites smpFavorites;
    private final String serverName;
    private final boolean openSync;
    private final int playerSlots;
    private final double money;
    private final String trackId;

    public FeatureServerGUI(BoxUtil util, int playerSlots, String serverName, SMPFavorites smpFavorites, double money, SerializedGUI serializedGUI, String trackId, boolean openSync) {
        super(util.config.inventories().featureServerGUIConfig, util);
        this.config = configManager.inventories().featureServerGUIConfig;
        this.serializedGUI = serializedGUI;
        this.smpFavorites = smpFavorites;
        this.playerSlots = playerSlots;
        this.serverName = serverName;
        this.openSync = openSync;
        this.trackId = trackId;
        this.money = money;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InventoryUtils.fillInventory(inventory, config.background);

        ConfigFeature configFeature = util.reactiveService.getFeatureTimer();

        setItem(config.confirm, Arrays.asList(
                new Placeholder("feature_price", configFeature.price),
                new Placeholder("feature_duration", StringUtils.getTimedMessage(
                        configManager.messages().timeFormat,
                        configFeature.featureDuration.getEffectiveTime(),
                        configManager
                ))
        ));

        setItem(config.goBack);
        setItem(config.closeGUI);

        return inventory;
    }


    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if(!isClickingCreatedGUI(event)) return;

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if(isItem(slot, config.goBack)){
            player.closeInventory();
            new SelectServerToFeatureGUI(util, player.getName(), playerSlots, smpFavorites, money, serializedGUI, openSync).openFuture(player);
        }else if(isItem(slot, config.closeGUI)){
            player.closeInventory();
        }else if(isItem(slot, config.confirm)){
            player.closeInventory();
            util.scheduler.runTaskAsynchronously(util.plugin, () -> {
                ConfigFeature configFeature = util.reactiveService.getFeatureTimer();

                SMPServer server = getServer(player.getUniqueId(), serverName, util);

                if(server == null) {
                    player.sendMessage(StringUtils.color(configManager.messages().unexpectedErrorFeaturing));
                    return;
                }

                List<SMPServer> featured = getAllServers(player.getUniqueId(), util).stream()
                        .filter(sv -> ServerUtils.getRemainingTime(sv) > 0)
                        .collect(Collectors.toList());

                if(featured.size() >= configManager.inventories().mainMenuGUIConfig.featuredSlots.size()){
                    player.sendMessage(StringUtils.color(configManager.messages().noMoreSpaceToFeature));
                }else{
                    double price = configFeature.price;

                    if(money < price){
                        player.sendMessage(StringUtils.color(configManager.messages().notEnoughMoneyToFeature));
                        return;
                    }

                    String message = StringUtils.getTimedMessage(
                            configManager.messages().timeFormat,
                            configFeature.featureDuration.getEffectiveTime(),
                            configManager
                    );

                    player.sendMessage(StringUtils.color(configManager.messages().successfullyAddedToFeature
                            .replace("%server_name%", serverName)
                            .replace("%feature_price%", String.valueOf(configFeature.price))
                            .replace("%time_format%", message)));


                    server.setDelay(configFeature.featureDuration.getEffectiveTime());

                    server.setLastTime(System.currentTimeMillis());

                    util.redisService.publishUpdate(server);

                    String tmMessage = util.persist.toString(new TMMessage(player.getName(), price), Persist.PersistType.JSON);

                    util.redisService.publish(MessageReceiver.TOKEN_MANAGER_RECEIVER.getName(), tmMessage);
                }
            });
        }
    }
}
