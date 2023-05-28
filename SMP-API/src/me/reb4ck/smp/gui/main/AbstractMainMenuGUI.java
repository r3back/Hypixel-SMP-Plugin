package me.reb4ck.smp.gui.main;

import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.gui.GUI;
import me.reb4ck.smp.gui.allservers.AllServersGUI;
import me.reb4ck.smp.gui.feature.SelectServerToFeatureGUI;
import me.reb4ck.smp.message.FavoriteMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.FavoriteGetter;
import me.reb4ck.smp.utils.SMPGetter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;


public abstract class AbstractMainMenuGUI extends GUI implements SMPGetter, FavoriteGetter {
    protected final Map<Integer, String> favoritesMap = new HashMap<>();
    protected final Map<Integer, String> featuredMap = new HashMap<>();
    protected final List<Integer> featuredAvailable = new ArrayList<>();

    protected final SerializedGUI serializedGUI;
    protected final MainMenuGUIConfig config;
    protected SMPFavorites smpFavorites;
    protected final String playerName;
    protected final boolean openSync;
    protected int favoriteSlots;
    protected double money;
    protected String trackId;


    public AbstractMainMenuGUI(BoxUtil util, String playerName, SerializedGUI serializedGUI, boolean openSync) {
        super(util.config.inventories().mainMenuGUIConfig, util);
        this.favoriteSlots = serializedGUI.getInteger(SerializedGUI.Value.FAVORITE_SLOTS);
        this.money = serializedGUI.getDouble(SerializedGUI.Value.TOKEN_MANAGER);
        this.trackId = serializedGUI.getString(SerializedGUI.Value.TRACK_ID);
        this.config = configManager.inventories().mainMenuGUIConfig;
        this.serializedGUI = serializedGUI;
        this.playerName = playerName;
        this.openSync = openSync;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if(!isClickingCreatedGUI(event)) return;

        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();

        if(isItem(slot, config.createServer)) {
            player.closeInventory();
            player.performCommand("smp create");
        }else if(isItem(slot, config.closeGUI)){
            player.closeInventory();
        }else if(isItem(slot, config.allServers)){
            if(trackId == null) return;

            player.closeInventory();
            new AllServersGUI(util, 1, favoriteSlots, smpFavorites, money, serializedGUI, trackId, openSync).openFuture(player);
        }else if(isItem(slot, config.joinRandomServer)){
            player.closeInventory();
            util.scheduler.runTaskAsynchronously(util.plugin, () -> {
                List<SMPServer> smpServers = getAllServers(player.getUniqueId(), util);

                if(smpServers == null || smpServers.size() == 0) return;

                int size = new Random().nextInt(smpServers.size());

                util.tpService.teleport(player, smpServers.get(size), 0);
            });
        }else if(favoritesMap.containsKey(slot)){
            if(!event.getClick().isRightClick()) return;

            String server = favoritesMap.get(slot);

            if(smpFavorites == null) return;

            smpFavorites.getFavorites().remove(server);

            FavoriteMessage favoriteMessage = FavoriteMessage
                    .builder()
                    .actionType(FavoriteMessage.ActionType.REMOVE).server(server)
                    .name(playerName)
                    .build();

            String message = util.persist.toString(favoriteMessage, Persist.PersistType.JSON);

            util.redisService.publish(Channel.BUKKIT_TO_BUNGEE_FAVORITES.getName(), message);

            if(openSync){
                player.openInventory(new LobbyMainMenuGUI(util, player.getName(), serializedGUI, openSync).getInventory());
            }else{
                player.closeInventory();
                new FutureMainMenuGUI(util, player.getName(), serializedGUI, openSync).openFuture(player);
            }

        }else if(featuredMap.containsKey(slot)){
            player.closeInventory();

            util.scheduler.runTaskAsynchronously(util.plugin, () -> {
                SMPServer server = getServer(player, featuredMap.get(slot), util);

                if(server == null) return;

                util.tpService.teleport(player, server, 0);
            });

        }else if(featuredAvailable.contains(slot)){
            player.closeInventory();

            new SelectServerToFeatureGUI(util, playerName, favoriteSlots, smpFavorites, money, serializedGUI, openSync).openFuture(player);
        }


    }
}
