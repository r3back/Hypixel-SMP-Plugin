package me.reb4ck.smp.api.hooks;

import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SoftwareDependPlugin {
    protected final JavaPlugin plugin;

    public SoftwareDependPlugin(JavaPlugin plugin, String displayName){
        this.plugin = plugin;
        Bukkit.getConsoleSender().sendMessage(StringUtils.color("&e"+ plugin.getDescription().getName() +" &aSuccessfully hooked into " + displayName +"!"));
    }
}
