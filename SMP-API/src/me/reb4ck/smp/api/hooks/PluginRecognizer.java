package me.reb4ck.smp.api.hooks;

import org.bukkit.Bukkit;

import java.util.List;

public abstract class PluginRecognizer {
    protected boolean isPlugin(String name) {
        return (Bukkit.getServer().getPluginManager().getPlugin(name) != null);
    }

    protected String getPluginVersion(String name) {
        return Bukkit.getServer().getPluginManager().getPlugin(name).getDescription().getVersion();
    }

    protected List<String> getPluginAuthor(String name) {
        return Bukkit.getServer().getPluginManager().getPlugin(name).getDescription().getAuthors();
    }
}
