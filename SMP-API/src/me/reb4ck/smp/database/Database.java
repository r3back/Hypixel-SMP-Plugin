package me.reb4ck.smp.database;

import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.user.SMPFavorites;

import java.util.List;

public interface Database {
    void createTables();

    void close();

    void saveSMPServer(SMPServer smpServer);

    SMPServer getSMPServer(String name);

    List<SMPServer> getServers();

    void saveSMPFavorites(SMPFavorites smpFavorites);

    SMPFavorites getSMPFavorites(String name);

    List<SMPFavorites> getFavorites();

    void deleteSMPServer(String name);

    double getTokens(String name);
}
