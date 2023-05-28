package me.reb4ck.smp.database.handler;

import com.google.inject.Inject;
import me.reb4ck.smp.api.factory.DatabaseFactory;
import me.reb4ck.smp.api.service.FavoriteService;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.database.Database;
import me.reb4ck.smp.database.future.FutureDatabase;
import me.reb4ck.smp.api.tracker.SMPRestAPI;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.database.future.SecondFuture;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Optional;

public final class DBFavoritesHandler implements DBHandler<Player> {
    private final FavoriteService favoriteService;
    private final FutureDatabase futureDatabase;
    private final BukkitScheduler scheduler;
    private final SMPRestAPI restAPI;
    private final JavaPlugin plugin;
    private final Database database;

    @Inject
    public DBFavoritesHandler(FavoriteService favoriteService, JavaPlugin plugin, FutureDatabase futureDatabase, DatabaseFactory factory, BukkitScheduler scheduler, SMPRestAPI restAPI) {
        this.plugin = plugin;
        this.restAPI = restAPI;
        this.scheduler = scheduler;
        this.futureDatabase = futureDatabase;
        this.database = factory.getDatabase();
        this.favoriteService = favoriteService;

        loadPlayerDataOnEnable();
    }

    @Override
    public void saveDataRepeatedly(int minutes) {
        scheduler.runTaskTimerAsynchronously(plugin, this::saveAllData, 0, minutes * 20L);
    }

    @Override
    public void disable() {
        saveAllData();
    }

    @Override
    public void saveData(Player player, boolean removeFromCache, boolean async) {
        final String name = player.getName();
        if (async)
            scheduler.runTaskAsynchronously(plugin, () -> save(name, removeFromCache));
        else
            save(name, removeFromCache);
    }

    @Override
    public void loadData(Player player) {
        scheduler.runTaskAsynchronously(plugin, () -> addUser(player));
    }

    @Override
    public void deleteData(Player object) {

    }

    private void loadData(SMPFavorites user) {
        scheduler.runTaskAsynchronously(plugin, () -> addUser(user));
    }

    /*
     * PRIVATE
     */
    private void addUser(Player player){
        ITrackID trackID = restAPI.getTrackId(player.getName());

        SMPFavorites favorites = database.getSMPFavorites(trackID.getValue());

        boolean isFirstTime = favorites == null;

        if(favorites == null) favorites = new SMPFavorites(trackID.getValue(), new ArrayList<>(), false);

        favoriteService.addFavorite(favorites);

        if(isFirstTime)
            database.saveSMPFavorites(favorites);
    }

    private void addUser(SMPFavorites user){
        Optional.ofNullable(user).ifPresent(favoriteService::addFavorite);
    }

    private void save(String name, boolean removeFromCache){
        Optional<SMPFavorites> favorites = favoriteService.getFavorites(name, FavoriteService.ToSearch.NAME);

        if(favorites.isPresent()){
            SMPFavorites smpFavorites = favorites.get();

            if(smpFavorites == null) return;

            database.saveSMPFavorites(smpFavorites);

            if(removeFromCache)
                favoriteService.removeUser(smpFavorites);
        }
    }

    private void loadPlayerDataOnEnable() {
        futureDatabase.getFuture().thenRun(() -> scheduler.runTaskAsynchronously(plugin, () -> database.getFavorites().forEach(this::loadData)));
    }

    private void saveAllData(){
        favoriteService.getFavorites().forEach(database::saveSMPFavorites);
    }
}
