package me.reb4ck.smp.utils;

import me.reb4ck.smp.api.placeholder.IPlaceholder;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.markable.MarkableImpl;
import me.reb4ck.smp.base.placeholder.Placeholder;

import java.util.Arrays;
import java.util.List;

public final class ServerUtils {
    public static long getRemainingTime(MarkableImpl smpServer){
        return smpServer.getLastMarked() + smpServer.getDelay() - System.currentTimeMillis();
    }

    public static long getRemainingTime(SMPServer smpServer){
        return smpServer.getLastTime() + smpServer.getDelay() - System.currentTimeMillis();
    }

   public static List<IPlaceholder> getPlaceholders(SMPServer smpServer){
        return Arrays.asList(new Placeholder("server_players", smpServer.getPlayers())
                , new Placeholder("server_max_players", smpServer.getMaxPlayers())
                , new Placeholder("server_name", smpServer.getName())
                , new Placeholder("server_display_name", smpServer.getDisplayName())
                , new Placeholder("server_owner", smpServer.getOwnerName())
        );
    }

    public static List<IPlaceholder> getPlaceholders(SMPServer smpServer, String off, String on){
        return Arrays.asList(new Placeholder("server_players", smpServer.getPlayers())
                , new Placeholder("server_max_players", smpServer.getMaxPlayers())
                , new Placeholder("server_name", smpServer.getName())
                , new Placeholder("server_owner", smpServer.getOwnerName())
        );
    }
}
