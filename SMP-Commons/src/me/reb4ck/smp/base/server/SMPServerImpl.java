package me.reb4ck.smp.base.server;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.server.SMPLogo;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public final class SMPServerImpl implements SMPServer {
    private String name;
    private String uuid;
    private String ip;
    private int port;
    private boolean enabled;
    private int ram;
    private String ownerName;
    private String password;
    private SMPUpgrade smpUpgrade;
    private int players;
    private List<String> description;
    private int maxPlayers;
    private SMPLogo logo;
    private long lastTime;
    private long delay;
    private String displayName;
}
