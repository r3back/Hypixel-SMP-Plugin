package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.exception.server.NotOnlineException;
import me.reb4ck.smp.api.service.TeleportService;
import me.reb4ck.smp.server.SMPServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Singleton
public class TeleportServiceImpl implements TeleportService {
    private final Plugin plugin;
    private final ProxyServer proxyServer;
    private final TaskScheduler taskScheduler;

    @Inject
    public TeleportServiceImpl(Plugin plugin, ProxyServer proxyServer, TaskScheduler taskScheduler) {
        this.plugin = plugin;
        this.proxyServer = proxyServer;
        this.taskScheduler = taskScheduler;
    }


    @Override
    public CompletableFuture<Void> teleport(ProxiedPlayer player, SMPServer smpServer, int delayInSeconds) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if(delayInSeconds > 0)
            taskScheduler.schedule(plugin, () -> teleport(player, smpServer, future), delayInSeconds, TimeUnit.SECONDS);
        else
            teleport(player, smpServer, future);

        return future;
    }

    @Override
    public void teleport(ProxiedPlayer player, SMPServer smpServer) {
        teleport(player, smpServer, 0);
    }

    @Override
    public void teleport(ProxiedPlayer player, String server) {
        ServerInfo serverInfo = ProxyServer.getInstance().getServers().get(server);

        if(serverInfo == null) return;

        player.connect(serverInfo);
    }

    @Override
    public CompletableFuture<Void> teleportAllPlayers(SMPServer smpServer, String server) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        ServerInfo serverInfo = getServerInfo(smpServer);

        if(serverInfo == null) return future;

        ServerInfo toTeleport = ProxyServer.getInstance().getServers().get(server);

        serverInfo.getPlayers().forEach(player -> player.connect(toTeleport));

        taskScheduler.schedule(plugin, () -> future.complete(null),1, TimeUnit.SECONDS);

        return future;
    }


    private void teleport(ProxiedPlayer player, SMPServer smpServer, CompletableFuture<Void> completableFuture) {
        ServerInfo serverInfo = proxyServer.constructServerInfo(smpServer.getName(), new InetSocketAddress(smpServer.getIp(), smpServer.getPort()), "", false);

        //player.connect(serverInfo);


        if(serverInfo == null) {
            completableFuture.completeExceptionally(new NotOnlineException());
            return;
        }

        player.connect(serverInfo);

        completableFuture.complete(null);
    }

    private ServerInfo getServerInfo(SMPServer smpServer){
        return proxyServer.getServers().computeIfAbsent(smpServer.getName(), s -> construct(smpServer));
    }

    private ServerInfo construct(SMPServer smpServer){
        return proxyServer.constructServerInfo(smpServer.getName(), new InetSocketAddress(smpServer.getIp(), smpServer.getPort()), "", false);
    }
}
