package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.api.service.DatabaseService;
import me.reb4ck.smp.database.handler.DBHandler;
import org.bukkit.entity.Player;

@Singleton
public final class DatabaseServiceImpl implements DatabaseService {
    private final DBHandler<Player> smpFavoritesDBHandler;
    private final DBHandler<SMPServer> smpHandler;

    @Inject
    public DatabaseServiceImpl(DBHandler<Player> smpFavoritesDBHandler, DBHandler<SMPServer> smpHandler) {
        this.smpFavoritesDBHandler = smpFavoritesDBHandler;
        this.smpHandler = smpHandler;
    }

    @Override
    public void saveSMPServer(SMPServer smpServer) {
        smpHandler.saveData(smpServer, false, true);
    }

    @Override
    public void deleteSMPServer(SMPServer server) {
        smpHandler.deleteData(server);
    }

    @Override
    public void saveUserData(Player player, boolean removeFromCache, boolean async){
        smpFavoritesDBHandler.saveData(player, removeFromCache, async);
    }

    @Override
    public void loadUserData(Player player){
        smpFavoritesDBHandler.loadData(player);
    }

    @Override
    public void disable() {
        smpFavoritesDBHandler.disable();
        smpHandler.disable();
    }
}