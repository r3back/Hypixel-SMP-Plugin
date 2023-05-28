package me.reb4ck.smp.database;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.base.config.*;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.server.SMPServerImpl;
import me.reb4ck.smp.base.user.SMPFavorites;
import me.reb4ck.smp.database.credentials.Credentials;
import me.reb4ck.smp.database.future.FutureDatabase;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import me.reb4ck.smp.persist.Persist.PersistType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SQLDatabase extends DatabaseImpl {
    protected final Persist persist;
    protected final JavaPlugin plugin;
    protected final BukkitScheduler scheduler;
    protected final FutureDatabase futureDatabase;
    protected final PersistType type = PersistType.JSON;

    @Inject
    public SQLDatabase(JavaPlugin plugin, ConfigManager<Configuration, Messages, Commands, Inventories> configManager, Persist persist, BukkitScheduler scheduler, FutureDatabase futureDatabase) {
        //super(plugin, Credentials.fromConfig(configManager.config().configDatabase), Credentials.fromConfig(configManager.config().tokenManagerDatabase));
        super(plugin, Credentials.fromConfig(configManager.config().privateConfigDatabase), Credentials.fromConfig(configManager.config().privateTokenManagerDatabase));

        this.plugin = plugin;
        this.persist = persist;
        this.scheduler = scheduler;
        this.futureDatabase = futureDatabase;
        createTables();
    }

    @Override
    public void createTables() {
        scheduler.runTaskAsynchronously(plugin, () -> {
            execute("CREATE TABLE IF NOT EXISTS " + smpServersTableName + "(NAME varchar(36), data text, primary key (NAME))");
            execute("CREATE TABLE IF NOT EXISTS " + smpFavoritesTableName + "(NAME varchar(36), data text, primary key (NAME))");
            futureDatabase.complete();
        });
    }

    @Override
    public SMPServer getSMPServer(String name) {
        try (Connection con = this.smpConnection.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + smpServersTableName + " WHERE " + settlementsColName + "=?")) {
            statement.setString(1, name);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()){
                    return getObject(SMPServerImpl.class, set.getString("data"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SMPServer> getServers(){
        List<SMPServer> settlements = new ArrayList<>();

        try (Connection con = this.smpConnection.getConnection(); PreparedStatement statement =
                con.prepareStatement("SELECT * FROM "  + smpServersTableName)) {
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()){
                    Optional.ofNullable(getObject(SMPServerImpl.class, set.getString("data"))).ifPresent(settlements::add);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return settlements;
    }


    @Override
    public SMPFavorites getSMPFavorites(String name) {
        try (Connection con = this.smpConnection.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + smpFavoritesTableName + " WHERE " + smpFavoritesColName + "=?")) {
            statement.setString(1, name);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()){
                    return getObject(SMPFavorites.class, set.getString("data"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SMPFavorites> getFavorites() {
        List<SMPFavorites> settlements = new ArrayList<>();
        try (Connection con = this.smpConnection.getConnection(); PreparedStatement statement =
                con.prepareStatement("SELECT * FROM "  + smpFavoritesTableName)) {
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()){
                    Optional.ofNullable(getObject(SMPFavorites.class, set.getString("data"))).ifPresent(settlements::add);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return settlements;
    }

    @Override
    public void deleteSMPServer(String name) {
        this.execute("DELETE FROM " + smpServersTableName + " WHERE " + settlementsColName + "=?", name);
    }

    @Override
    public double getTokens(String name) {
        try (Connection con = this.tmConnection.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + this.tokenManagerTable + " WHERE name=?")) {
            statement.setString(1, name);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()){
                    return set.getDouble("tokens");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void saveSMPServer(SMPServer smpServer) {
        this.execute("INSERT IGNORE INTO " + smpServersTableName + " VALUES(?,?)", smpServer.getName(), "null");
        this.execute("UPDATE " + smpServersTableName + " SET data =? WHERE " + settlementsColName + "=?", persist.toString(smpServer, type), smpServer.getName());
    }

    @Override
    public void saveSMPFavorites(SMPFavorites smpFavorites) {
        this.execute("INSERT IGNORE INTO " + smpFavoritesTableName + " VALUES(?,?)", smpFavorites.getUuid(), "null");
        this.execute("UPDATE " + smpFavoritesTableName + " SET data =? WHERE " + smpFavoritesColName + "=?", persist.toString(smpFavorites, type), smpFavorites.getUuid());
    }

    protected  <T> T getObject(Class<T> clazz, String str){
        return str == null || str.equals("") ? null : persist.load(clazz, str, type);
    }
}
