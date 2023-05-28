package me.reb4ck.smp.api.service;


import com.google.inject.ImplementedBy;
import me.reb4ck.smp.base.service.DatabaseServiceImpl;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.entity.Player;

@ImplementedBy(DatabaseServiceImpl.class)
public interface DatabaseService {
    void saveSMPServer(SMPServer smpServer);

    void deleteSMPServer(SMPServer smpServer);

    void saveUserData(Player player, boolean removeFromCache, boolean async);

    void loadUserData(Player player);

    void disable();
}
