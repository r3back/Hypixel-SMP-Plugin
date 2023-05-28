package me.reb4ck.smp.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public interface ServerGetter {
    default String getServerName(JavaPlugin plugin){
        String serverName = "testing";

        try (InputStream input = new FileInputStream(plugin.getDataFolder().getAbsolutePath().toString() + "/../../server.properties")) {

            Properties prop = new Properties();

            prop.load(input);

            serverName = Optional.ofNullable(prop.getProperty("server-name")).orElse("lobby");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return serverName;
    }
}
