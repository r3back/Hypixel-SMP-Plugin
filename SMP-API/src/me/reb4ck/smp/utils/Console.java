package me.reb4ck.smp.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Console {
    private static String PLUGIN_NAME;
    private static final Logger logger = LoggerFactory.getLogger(Console.class);

    public static void init(JavaPlugin instance){
        if(Console.PLUGIN_NAME == null)
            Console.PLUGIN_NAME = instance.getDescription().getName();
    }

    public static void sendMessage(String message, MessageType messageType) {
        (new ConsoleMessage()).sendMessage("[" + PLUGIN_NAME + "] " + message, messageType);
    }

    public static void sendMessage(String message) {
        (new ConsoleMessage()).sendMessage("[" + PLUGIN_NAME + "] " + message, MessageType.COLORED);
    }

    private static class ConsoleMessage {
        public void sendMessage(String message, MessageType messageType) {
            if (messageType == MessageType.COLORED)
                Bukkit.getConsoleSender().sendMessage(StringUtils.color(message));
            else
                logger.warn(StringUtils.color(message));
        }
    }

    public enum MessageType {
        COLORED;
    }
}
