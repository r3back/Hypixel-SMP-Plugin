package me.reb4ck.smp.listener;

import com.google.inject.Inject;
import me.reb4ck.smp.api.placeholder.IPlaceholder;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.placeholder.Placeholder;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.ServerGetter;
import me.reb4ck.smp.utils.ServerUtils;
import me.reb4ck.smp.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class PlayerJoinListener implements Listener, ServerGetter, SMPGetter {
    private final BukkitScheduler bukkitScheduler;
    private final String serverName;
    private final BoxUtil boxUtil;

    @Inject
    public PlayerJoinListener(BukkitScheduler bukkitScheduler, BoxUtil boxUtil) {
        this.serverName = getServerName(boxUtil.plugin);
        this.bukkitScheduler = bukkitScheduler;
        this.boxUtil = boxUtil;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        final String name = player.getName();

        bukkitScheduler.runTaskAsynchronously(boxUtil.plugin, () -> {
            ITrackID iTrackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            String uuid = iTrackID.getValue();

            SMPServer server = getServer(player, serverName, uuid, boxUtil);

            if(server == null) return;

            if(player.isOp()) {
                List<IPlaceholder> placeholders = Arrays.asList(new Placeholder("username", server.getName()), new Placeholder("password", server.getPassword()));

                boxUtil.config.messages().ftpData.forEach(message -> player.sendMessage(StringUtils.processMultiplePlaceholders(message, placeholders)));

                sendPassWord(player, server);
            }

            if(!player.isOp())
                bukkitScheduler.runTask(boxUtil.plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "op " + name));


            List<IPlaceholder> placeholders = ServerUtils.getPlaceholders(server);

            Optional.ofNullable(player)
                    .ifPresent(player1 -> boxUtil.config.messages().adminMessage.forEach(msg -> player1.sendMessage(StringUtils.processMultiplePlaceholders(msg, placeholders))));


        });
    }

    private void sendPassWord(Player player, SMPServer smpServer){
        String message = boxUtil.config.messages().ftpPassword.action.replace("%password%", smpServer.getPassword());

        TextComponent textComponent = new TextComponent(StringUtils.color(message));

        if (message != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, smpServer.getPassword()));
        }
        if (message != null) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringUtils.color(boxUtil.config.messages().ftpPassword.aboveMessage)).create()));
        }
        player.spigot().sendMessage(textComponent);
    }
}
