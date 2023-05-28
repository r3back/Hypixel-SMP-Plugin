package me.reb4ck.smp.redis;

import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.message.SMPMessage.ActionType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public interface RedisService {
    void connect();

    void disable();

    void publish(String channel, String message);

    void publish(String channel, Message message);

    void publishUpdate(SMPServer server);

    void publishUpdate(SMPServer server, ActionType actionType);

    void publishSync(String channel, String message);

    default String getServer(File file){
        String serverName = "testing";
        try (InputStream input = new FileInputStream(file.getAbsolutePath().toString() + "/../../server.properties")) {

            Properties prop = new Properties();

            prop.load(input);

            serverName = Optional.ofNullable(prop.getProperty("server-name")).orElse("lobby");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return serverName;

    }

    public interface Message{

    }
}
