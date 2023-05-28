package me.reb4ck.smp.api.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.reb4ck.smp.server.SMPServer;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class ServerList {
    private List<SMPServer> smpServerList;
}
