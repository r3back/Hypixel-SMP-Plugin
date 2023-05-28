package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import me.reb4ck.smp.api.service.DatabaseService;
import me.reb4ck.smp.api.service.ProxyService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.service.TaskService;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class TaskServiceImpl implements TaskService {
    private final DatabaseService databaseService;
    private final BukkitScheduler taskScheduler;
    private final ProxyService proxyService;
    private final SMPService smpService;
    private final JavaPlugin plugin;

    @Inject
    public TaskServiceImpl(BukkitScheduler taskScheduler, SMPService smpService, JavaPlugin plugin, DatabaseService databaseService, ProxyService proxyService) {
        this.databaseService = databaseService;
        this.taskScheduler = taskScheduler;
        this.proxyService = proxyService;
        this.smpService = smpService;
        this.plugin = plugin;
        startTasks();
    }

    @Override
    public void startTasks() {
        taskScheduler.runTaskTimerAsynchronously(plugin, this::updateServerPlayers,0, 3 * 20L);
        taskScheduler.runTaskTimerAsynchronously(plugin, this::updateServerStatus, 0, 10 * 20L);
    }

    private void updateServerPlayers(){
        taskScheduler.runTaskAsynchronously(plugin, () -> {
            for (SMPServer smpServer : smpService.getServers()) {
                try {
                    /*ServerInfo serverInfo = getServerInfo(smpServer);

                    if (serverInfo == null) continue;

                    int size = (int) ProxyServer.getInstance().getPlayers().stream().filter(player -> player.getServer().getInfo().getName().equals(smpServer.getName())).count();

                    if (size == smpServer.getPlayers()) continue;

                    smpServer.setPlayers(size);

                    databaseService.saveSMPServer(smpServer);*/
                }catch (Exception ignored){

                }
            }
        });
    }

    private void updateServerStatus(){
        for (SMPServer smpServer : smpService.getServers()) {
            try {
                if(!smpServer.isEnabled()) continue;

                boolean online = proxyService.isOnline(smpServer);

                if(online == smpServer.isEnabled()) continue;

                smpServer.setEnabled(online);

                databaseService.saveSMPServer(smpServer);
            }catch (Exception ignored){
            }
        }
    }

    /*private ServerInfo getServerInfo(SMPServer smpServer){
        return ProxyServer.getInstance().constructServerInfo(smpServer.getName(), new InetSocketAddress(smpServer.getIp(), smpServer.getPort()), "", false);
    }*/
}
