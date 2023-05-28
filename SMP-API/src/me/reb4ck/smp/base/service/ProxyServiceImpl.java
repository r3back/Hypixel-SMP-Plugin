package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.exception.port.NoPortAvailableException;
import me.reb4ck.smp.api.service.ProxyService;
import me.reb4ck.smp.base.config.*;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.server.SMPServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public final class ProxyServiceImpl implements ProxyService {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    private final SMPService smpService;

    @Inject
    public ProxyServiceImpl(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, SMPService smpService){
        this.configManager = configManager;
        this.smpService = smpService;
    }

    @Override
    public Integer getAvailablePort() throws NoPortAvailableException {
        List<Integer> usedPorts = smpService
                .getServers()
                .stream()
                .map(SMPServer::getPort)
                .collect(Collectors.toList());

        return configManager.config().availablePorts.stream()
                .filter(port -> !usedPorts.contains(port))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .orElseThrow(NoPortAvailableException::new);
    }

    @Override
    public boolean isOnline(SMPServer smpServer) {
        try {
            Socket s = new Socket();

            s.connect(new InetSocketAddress(smpServer.getIp(), smpServer.getPort()), 20);

            s.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
