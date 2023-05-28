package me.reb4ck.smp.api.service;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.base.service.TeleportServiceImpl;
import me.reb4ck.smp.api.exception.server.AlreadyOnlineException;
import me.reb4ck.smp.api.exception.server.OnlineLimitException;
import me.reb4ck.smp.server.SMPServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.CompletableFuture;

@ImplementedBy(TeleportServiceImpl.class)
public interface TeleportService {
    CompletableFuture<Void> teleport(ProxiedPlayer player, SMPServer smpServer, int delayInSeconds);

    void teleport(ProxiedPlayer player, SMPServer smpServer) throws AlreadyOnlineException, OnlineLimitException;

    void teleport(ProxiedPlayer player, String server);

    CompletableFuture<Void> teleportAllPlayers(SMPServer smpServer, String server);
}
