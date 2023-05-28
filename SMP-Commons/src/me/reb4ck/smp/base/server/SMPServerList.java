package me.reb4ck.smp.base.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.reb4ck.smp.server.SMPServer;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SMPServerList {
    private List<SMPServer> serverList;
}
