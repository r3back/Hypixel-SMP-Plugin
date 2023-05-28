package me.reb4ck.smp.api.service;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.api.exception.port.NoPortAvailableException;
import me.reb4ck.smp.base.service.ProxyServiceImpl;
import me.reb4ck.smp.server.SMPServer;

@ImplementedBy(ProxyServiceImpl.class)
public interface ProxyService {
    Integer getAvailablePort() throws NoPortAvailableException;

    boolean isOnline(SMPServer smpServer);
}
