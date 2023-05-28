package me.reb4ck.smp.listener;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import me.reb4ck.smp.base.markable.MarkableImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;

public final class SMPListener implements Listener {
    private final JavaPlugin plugin;
    private final SMPService smpService;
    private final BukkitScheduler taskScheduler;
    private final PermissionsPlugin<Player> permissionsPlugin;
    private final Map<String, MarkableImpl> smpMap = new HashMap<>();
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;

    @Inject
    public SMPListener(JavaPlugin plugin, SMPService smpService, BukkitScheduler taskScheduler, ConfigManager<Configuration, Messages, Commands, Inventories> configManager,
                       PermissionsPlugin<Player> permissionsPlugin) {
        this.plugin = plugin;
        this.smpService = smpService;
        this.taskScheduler = taskScheduler;
        this.permissionsPlugin = permissionsPlugin;
        this.configManager = configManager;
        //initScheduler();
    }

    /*@EventHandler
    public void onQuit(ServerConnectEvent event){
        ServerInfo serverInfo = event.getTarget();

        if(configManager.config().blockedFunctionalityServers.contains(serverInfo.getName())) return;

        Optional<SMPServer> smpServer = smpService.getServer(serverInfo.getName());

        if(!smpServer.isPresent()) return;

        String name = smpServer.get().getName();

        smpMap.remove(name);
    }


    @EventHandler
    public void onQuit(ServerDisconnectEvent event){
        Player player = event.getPlayer();

        ServerInfo serverInfo = event.getTarget();

        final String name = event.getTarget().getName();

        if(configManager.config().blockedFunctionalityServers.contains(name)) return;

        final int players = serverInfo.getPlayers().size();

        taskScheduler.runAsync(plugin, () -> {
            Optional<SMPServer> smpServer = smpService.getServer(name);

            if(!smpServer.isPresent()) return;

            boolean stop = false;

            if(players <= 0){
                if(!smpMap.containsKey(name)){
                    boolean isUnlimited = permissionsPlugin.hasDefaultPermission(smpServer.get().getOwnerName(), configManager.config().unlimitedServerPermission);

                    if(isUnlimited) {
                        MarkableImpl markable = new MarkableImpl(configManager.config().shutdownUnlimitedServersAfter.getEffectiveTime(), System.currentTimeMillis());
                        smpMap.put(name, markable);
                        Console.sendMessage("&cNo Players remaining in SMP Server " + name + " Closing server in " + StringUtils.getTimedMessage("%days%d %hours%h %minutes%m %seconds%s", ServerUtils.getRemainingTime(markable)));
                    }else
                        stop = true;

                }
            }

            if(!stop) return;

            try {
                smpService.stopServer(player, smpServer.get());
            }catch (NotOnlineException e){
            }
        });
    }

    private void initScheduler(){
        taskScheduler.schedule(plugin, () -> {
            taskScheduler.runAsync(plugin, () -> {
                for(String key : smpMap.keySet()) {
                    MarkableImpl markable = smpMap.get(key);

                    if (ServerUtils.getRemainingTime(markable) > 0) continue;

                    Optional<SMPServer> smpServer = smpService.getServer(key);

                    if(!smpServer.isPresent()) {
                        smpMap.remove(key);
                        continue;
                    }

                    try {
                        smpMap.remove(key);

                        smpService.stopServer(smpServer.get());
                    }catch (NotOnlineException ignored){
                    }
                }
            });
        },0, 1, TimeUnit.SECONDS);
    }*/
}
