package me.reb4ck.smp.database;

import me.reb4ck.smp.database.credentials.Credentials;
import me.reb4ck.smp.database.executions.DatabaseExecution;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DatabaseImpl extends DatabaseExecution implements Database {
    public DatabaseImpl(JavaPlugin plugin, Credentials smpCredentials, Credentials tmCredentials) {
        super(plugin.getDataFolder(), smpCredentials,tmCredentials, "Bungee");
    }

    @Override
    public void close(){
        closeHikari();
    }
}
