package me.reb4ck.smp.server;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import me.reb4ck.smp.base.server.SMPServerImpl;
import me.reb4ck.smp.base.server.SMPUpgrade;

import java.util.List;

@JsonDeserialize(as = SMPServerImpl.class)
public interface SMPServer {
    String getName();
    String getDisplayName();
    String getUuid();
    List<String> getDescription();
    String getIp();
    int getPort();
    int getRam();
    String getOwnerName();
    String getPassword();
    SMPUpgrade getSmpUpgrade();
    int getPlayers();
    int getMaxPlayers();
    SMPLogo getLogo();
    boolean isEnabled();

    long getDelay();
    long getLastTime();
    void setDisplayName(String displayName);
    void setName(String name);
    void setUuid(String uuid);
    void setRam(int ram);
    void setOwnerName(String name);
    void setPassword(String password);
    void setSmpUpgrade(SMPUpgrade smpUpgrade);
    void setPlayers(int players);
    void setMaxPlayers(int maxPlayers);
    void setDescription(List<String> description);
    void setLogo(SMPLogo smpLogo);
    void setDelay(long delay);
    void setLastTime(long lastTime);
    void setEnabled(boolean enabled);
}
