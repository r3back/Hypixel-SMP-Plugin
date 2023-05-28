package me.reb4ck.smp.database.handler;

import com.google.inject.Inject;
import me.reb4ck.smp.api.factory.DatabaseFactory;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.database.Database;
import me.reb4ck.smp.database.future.FutureDatabase;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Optional;

public final class DBServerHandler implements DBHandler<SMPServer> {
    private final JavaPlugin plugin;
    private final Database database;
    private final SMPService smpService;
    private final BukkitScheduler scheduler;
    private final FutureDatabase futureDatabase;

    @Inject
    public DBServerHandler(SMPService smpService, JavaPlugin plugin, DatabaseFactory databaseFactory, BukkitScheduler scheduler, FutureDatabase futureDatabase) {
        this.plugin = plugin;
        this.scheduler = scheduler;
        this.smpService = smpService;
        this.futureDatabase = futureDatabase;
        this.database = databaseFactory.getDatabase();

        loadAllData();
    }

    @Override
    public void saveDataRepeatedly(int minutes) {
        /**
         * TODO
         */
        //scheduler.runTaskTimerAsynchronously(plugin, this::saveAllData, 0, 1200L * minutes);
    }

    @Override
    public void disable() {
        saveAllData();
        database.close();
    }

    @Override
    public void saveData(SMPServer settlement, boolean removeFromCache, boolean async) {
        if (async)
            scheduler.runTaskAsynchronously(plugin, () -> save(settlement));
        else
            save(settlement);
    }

    @Override
    public void loadData(SMPServer settlement) {
        scheduler.runTaskAsynchronously(plugin, () -> registerServer(settlement));
    }

    @Override
    public void deleteData(SMPServer object) {
        final String name = object.getName();
        scheduler.runTaskAsynchronously(plugin, () -> database.deleteSMPServer(name));
    }

    private void registerServer(SMPServer settlement){
        Optional.ofNullable(settlement).ifPresent(tBag -> smpService.addServer(settlement));
    }

    private void save(SMPServer settlement){
        Optional.ofNullable(settlement).ifPresent(database::saveSMPServer);
    }

    private void loadAllData() {
        futureDatabase.getFuture().thenRun(() -> scheduler.runTaskAsynchronously(plugin, () -> database.getServers().forEach(this::registerServer)));
    }

    private void saveAllData(){
        smpService.getServers().forEach(this::save);

    }
}
