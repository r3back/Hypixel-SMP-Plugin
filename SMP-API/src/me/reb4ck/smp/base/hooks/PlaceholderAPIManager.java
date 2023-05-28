package me.reb4ck.smp.base.hooks;

import com.google.inject.Inject;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.reb4ck.smp.api.hooks.PlaceholdersPlugin;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.utils.ServerGetter;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPIManager extends PlaceholderExpansion implements PlaceholdersPlugin, ServerGetter {
    private final SMPService smpService;
    private final JavaPlugin plugin;
    private final String server;

    @Inject
    public PlaceholderAPIManager(JavaPlugin plugin, SMPService smpService) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e"+ plugin.getDescription().getName() +" &aSuccessfully hooked into PlaceholdersAPI!"));
        this.server = getServerName(plugin);
        this.smpService = smpService;
        this.plugin = plugin;
        this.register();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }


    @NotNull
    @Override
    public String getIdentifier() {
        return "smp";
    }

    @NotNull
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null || identifier == null) {
            return "";
        }

        switch (identifier) {
            case "total_servers":
                return String.valueOf(smpService.getServers().size());
            case "online_servers":
                return String.valueOf((int) smpService.getServers().stream().filter(SMPServer::isEnabled).count());
            case "total_players":
                return String.valueOf(smpService.getServers().stream().mapToInt(SMPServer::getPlayers).sum());
            case "server_name":
                return server;
        }
        return null;
    }
}
