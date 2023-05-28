package me.reb4ck.smp.listener;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.service.DatabaseService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class BungeeCordListener implements Listener {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    private final DatabaseService database;

    @Inject
    public BungeeCordListener(DatabaseService databaseService, ConfigManager<Configuration, Messages, Commands, Inventories> configManager) {
        this.configManager = configManager;
        this.database = databaseService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        //if(event.getPlayer().getServer() == null) return;

        //if(configManager.config().blockedFunctionalityServers.contains(event.getPlayer().getServer().getInfo().getName())) return;

        database.loadUserData(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        //if(configManager.config().blockedFunctionalityServers.contains(event.getTarget().getName())) return;

        database.saveUserData(event.getPlayer(), false, true);
    }

    @EventHandler
    public void onQuit(PlayerKickEvent event){
        //if(event.getPlayer().getServer() == null) return;

        //if(configManager.config().blockedFunctionalityServers.contains(event.getPlayer().getServer().getInfo().getName())) return;

        database.saveUserData(event.getPlayer(), false, true);
    }
}
