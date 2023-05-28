package me.reb4ck.smp.api.service;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.api.exception.server.AlreadyOnlineException;
import me.reb4ck.smp.api.exception.server.OnlineLimitException;
import me.reb4ck.smp.base.service.TeleportServiceImpl;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@ImplementedBy(TeleportServiceImpl.class)
public interface TeleportService {
    CompletableFuture<Void> teleport(Player player, SMPServer smpServer, int delayInSeconds);

    //void teleport(Player player, SMPServer smpServer) throws AlreadyOnlineException, OnlineLimitException;

    void teleport(Player player, String server);

    CompletableFuture<Void> teleportAllPlayers(SMPServer smpServer, String server);
}
