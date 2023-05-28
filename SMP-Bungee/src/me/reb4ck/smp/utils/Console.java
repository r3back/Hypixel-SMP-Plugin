package me.reb4ck.smp.utils;

import me.reb4ck.smp.BungeeSMP;
import net.md_5.bungee.api.ProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Console {
    private static String PLUGIN_NAME;
    private static BungeeSMP INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(Console.class);

    public static void init(BungeeSMP INSTANCE){
        if(Console.INSTANCE == null) {
            Console.INSTANCE = INSTANCE;
            Console.PLUGIN_NAME = INSTANCE.getDescription().getName();
        }
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
                ProxyServer.getInstance().getConsole().sendMessage(StringUtils.color(message));
            else
                logger.warn(StringUtils.color(message));
        }
    }

    public enum MessageType {
        COLORED;
    }
}
