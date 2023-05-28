package me.reb4ck.smp.api.service;

import me.reb4ck.smp.api.exception.port.NoPortAvailableException;
import me.reb4ck.smp.api.exception.server.AlreadyOnlineException;
import me.reb4ck.smp.api.exception.server.OnlineLimitException;
import me.reb4ck.smp.api.exception.server.NotOnlineException;
import me.reb4ck.smp.message.CommandMessage;
import me.reb4ck.smp.message.SMPMessage;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface SMPService {
    SMPServer addServer(String ownerName, String uuid, String name, boolean canStart) throws NoPortAvailableException;

    void addServer(SMPServer smpServer);

    void deleteServer(SMPServer smpServer);

    void startServer(SMPServer smpServer) throws AlreadyOnlineException, OnlineLimitException;

    void stopServer(Player player, SMPServer smpServer) throws NotOnlineException;

    void stopServer(SMPServer smpServer) throws NotOnlineException;

    void updateServer(SMPMessage.ActionType actionType, SMPServer smpServer);

    void sendCommand(SMPServer smpServer, String command) throws NotOnlineException;

    void upgradeServer(SMPServer smpServer, int ram);

    Optional<SMPServer> getServer(String name);

    List<SMPServer> getServers(String uuid);

    List<SMPServer> getServers();

    boolean canStartServer();
}
