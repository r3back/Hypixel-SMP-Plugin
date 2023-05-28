package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.exception.server.AlreadyOnlineException;
import me.reb4ck.smp.api.exception.server.NotOnlineException;
import me.reb4ck.smp.api.exception.server.OnlineLimitException;
import me.reb4ck.smp.api.service.PasswordService;
import me.reb4ck.smp.api.service.ProxyService;
import me.reb4ck.smp.api.service.communicator.SenderCommunicatorService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.base.service.common.CommonSMPService;
import me.reb4ck.smp.message.SMPMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.entity.Player;

public final class PlayerSMPService extends CommonSMPService {
    @Inject
    public PlayerSMPService(ProxyService proxyService, Persist persist, PasswordService passwordService, SenderCommunicatorService communicatorService,
                            ConfigManager<Configuration, Messages, Commands, Inventories> configManager, RedisService redisService) {
        super(proxyService, persist, passwordService, communicatorService, configManager, redisService);
    }

    @Override
    public void updateServer(SMPMessage.ActionType actionType, SMPServer smpServer) {
        switch (actionType){
            case CREATE:
                smpServers.put(smpServer.getName(), smpServer);
                break;
            case UPDATE:
                if(!smpServers.containsKey(smpServer.getName())) return;
                smpServers.put(smpServer.getName(), smpServer);
                break;
            case DELETE:
                smpServers.remove(smpServer.getName());
                break;
        }
    }

    @Override
    public void startServer(SMPServer smpServer) throws AlreadyOnlineException, OnlineLimitException {
        if(smpServer.isEnabled()) throw new AlreadyOnlineException();

        if(!canStartServer()) throw new OnlineLimitException();

        smpServer.setEnabled(true);

        //databaseService.saveSMPServer(smpServer);

        communicatorService.startServer(smpServer);

        redisService.publishUpdate(smpServer);
    }

    @Override
    public void stopServer(Player player, SMPServer smpServer) throws NotOnlineException {
        if(!smpServer.isEnabled()) throw new NotOnlineException();

        smpServer.setEnabled(false);

        redisService.publishUpdate(smpServer);

        if(player != null) sendCommand(smpServer, "stop");

        communicatorService.stopServer(smpServer);
    }
}
