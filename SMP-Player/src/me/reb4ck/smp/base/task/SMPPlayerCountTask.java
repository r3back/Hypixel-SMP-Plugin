package me.reb4ck.smp.base.task;

import com.google.inject.Inject;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.task.SMPPlayerCount;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.ServerGetter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

public final class SMPPlayerCountTask implements SMPPlayerCount, ServerGetter, SMPGetter {

    private final String server;
    private final BoxUtil boxUtil;
    private final BukkitScheduler taskScheduler;

    @Inject
    public SMPPlayerCountTask(BoxUtil boxUtil, BukkitScheduler taskScheduler) {
        this.server = getServerName(boxUtil.plugin);
        this.taskScheduler = taskScheduler;
        this.boxUtil = boxUtil;

        start();
    }

    @Override
    public void start(){
        taskScheduler.runTaskTimerAsynchronously(boxUtil.plugin, () -> {
            SMPServer smpServer = getServer(UUID.randomUUID(), server, boxUtil);

            if(smpServer == null) return;

            if(smpServer.getPlayers() == Bukkit.getOnlinePlayers().size()) return;

            smpServer.setPlayers(Bukkit.getOnlinePlayers().size());

            boxUtil.redisService.publishUpdate(smpServer);
        },0, 40);
    }
}
