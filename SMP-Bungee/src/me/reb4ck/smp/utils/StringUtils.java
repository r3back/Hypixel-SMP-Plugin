package me.reb4ck.smp.utils;

import net.md_5.bungee.api.ChatColor;

public final class StringUtils {
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
