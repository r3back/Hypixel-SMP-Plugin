package me.reb4ck.smp.base.task;

import com.google.inject.Inject;
import me.reb4ck.smp.api.reactive.ReactiveService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.task.SMPShutdown;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.markable.MarkableImpl;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class SMPShutdownTask implements SMPShutdown, ServerGetter, Listener, SMPGetter {
    private final Map<String, MarkableImpl> smpMap = new HashMap<>();
    private final BukkitScheduler scheduler;
    private final BoxUtil boxUtil;
    private final String server;

    private boolean playerJoined = false;

    @Inject
    public SMPShutdownTask(BukkitScheduler scheduler, BoxUtil boxUtil) {
        this.server = getServerName(boxUtil.plugin);
        this.scheduler = scheduler;
        this.boxUtil = boxUtil;

        boxUtil.plugin.getServer().getPluginManager().registerEvents(this, boxUtil.plugin);

        start();
    }

    @Override
    public void start() {
        startChecking();
        initScheduler();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(!playerJoined)
            playerJoined = true;

    }

    private void startChecking() {
        scheduler.runTaskTimerAsynchronously(boxUtil.plugin, () -> {

            if(!boxUtil.reactiveService.isCompleted()) return;

            if(!playerJoined) return;

            int players = Bukkit.getOnlinePlayers().size();

            SMPServer smpServer = getServer(UUID.randomUUID(), server, boxUtil);

            if(smpServer == null) return;

            boolean stop = false;

            if(players <= 0){
                if(!smpMap.containsKey(server)){
                    boolean isUnlimited = boxUtil.reactiveService.isUnlimitedServer();

                    if(isUnlimited) {
                        MarkableImpl markable = new MarkableImpl(boxUtil.reactiveService.getShutDownTimer().getEffectiveTime(), System.currentTimeMillis());
                        smpMap.put(server, markable);
                        Console.sendMessage("&cNo Players remaining in SMP Server " + server +
                                " Closing server in " +
                                StringUtils.getTimedMessage("%days%d %hours%h %minutes%m %seconds%s", ServerUtils.getRemainingTime(markable)));
                    }else
                        stop = true;
                }
            }else{
                smpMap.remove(server);
            }

            if(!stop) return;

            scheduler.runTask(boxUtil.plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop"));

        }, 0, 20);
    }

    private void initScheduler(){
        scheduler.runTaskTimerAsynchronously(boxUtil.plugin, () -> {
            for(String key : smpMap.keySet()) {
                MarkableImpl markable = smpMap.get(key);

                if (ServerUtils.getRemainingTime(markable) > 0) continue;

                SMPServer smpServer = getServer(UUID.randomUUID(), key, boxUtil);

                if(smpServer == null) {
                    smpMap.remove(key);
                    continue;
                }

                smpMap.remove(key);

                scheduler.runTask(boxUtil.plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop"));

                }
        },0, 20);
    }

}
