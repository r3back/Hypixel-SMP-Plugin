package me.reb4ck.smp.base.service.common;

import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.exception.port.NoPortAvailableException;
import me.reb4ck.smp.api.exception.server.NotOnlineException;
import me.reb4ck.smp.api.service.PasswordService;
import me.reb4ck.smp.api.service.ProxyService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.service.communicator.SenderCommunicatorService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.base.server.SMPServerImpl;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.CommandMessage;
import me.reb4ck.smp.message.SMPMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPLogo;
import me.reb4ck.smp.server.SMPServer;

import java.util.*;
import java.util.stream.Collectors;

public abstract class CommonSMPService implements SMPService {
    protected final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    protected final Map<String, SMPServer> smpServers = new HashMap<>();
    protected final SenderCommunicatorService communicatorService;
    protected final PasswordService passwordService;

    protected final RedisService redisService;
    protected final ProxyService proxyService;
    protected final Persist persist;

    public CommonSMPService(ProxyService proxyService, Persist persist, PasswordService passwordService, SenderCommunicatorService communicatorService,
                            ConfigManager<Configuration, Messages, Commands, Inventories> configManager, RedisService redisService) {
        this.communicatorService = communicatorService;
        this.passwordService = passwordService;
        this.configManager = configManager;
        this.proxyService = proxyService;
        this.redisService = redisService;
        this.persist = persist;
    }

    @Override
    public SMPServer addServer(String ownerName, String uuid, String name, boolean canStart) throws NoPortAvailableException {
        Integer port = proxyService.getAvailablePort();

        SMPServer smpServer = SMPServerImpl.builder()
                .name(name)
                .uuid(uuid)
                .displayName(name)
                .port(port)
                .password(passwordService.generatePassayPassword())
                .ram(2)
                .enabled(canStart)
                .players(0)
                .maxPlayers(50)
                .ownerName(ownerName)
                .logo(new SMPLogo("DIAMOND_BLOCK", true))
                .description(configManager.messages().defaultDescription)
                .ip(configManager.config().hostIp)
                .build();

        addServer(smpServer);

        //databaseService.saveSMPServer(smpServer);

        communicatorService.createServer(smpServer);

        redisService.publishUpdate(smpServer, SMPMessage.ActionType.CREATE);

        return smpServer;
    }

    @Override
    public void addServer(SMPServer smpServer) {
        smpServers.put(smpServer.getName(), smpServer);
    }

    @Override
    public void deleteServer(SMPServer smpServer) {

        //databaseService.deleteSMPServer(smpServer);

        communicatorService.deleteServer(smpServer);

        communicatorService.updateVirtualUsers();

        smpServers.remove(smpServer.getName());

        redisService.publishUpdate(smpServer, SMPMessage.ActionType.DELETE);

        redisService.publish(Channel.REMOVE_FROM_BUNGEE.getName(), smpServer.getName());
    }



    @Override
    public void stopServer(SMPServer smpServer) throws NotOnlineException {

        if(!smpServer.isEnabled()) throw new NotOnlineException();

        smpServer.setEnabled(false);

        communicatorService.stopServer(smpServer);

    }

    @Override
    public void sendCommand(SMPServer smpServer, String command) throws NotOnlineException {

        if(!smpServer.isEnabled()) throw new NotOnlineException();


        CommandMessage consoleMessage = CommandMessage.builder()
                .command(command)
                .sender(CommandMessage.Sender.CONSOLE)
                .receiver(smpServer.getName())
                .build();

        String str = persist.toString(consoleMessage, Persist.PersistType.JSON);

        redisService.publish(Channel.COMMANDS.getName(), str);
    }

    @Override
    public void upgradeServer(SMPServer smpServer, int ram) {
        communicatorService.updateServer(smpServer, ram);
    }

    @Override
    public List<SMPServer> getServers(String uuid) {
        return smpServers.values().stream().filter(smpServer -> smpServer.getUuid().equals(uuid)).collect(Collectors.toList());
    }

    @Override
    public List<SMPServer> getServers() {
        return new ArrayList<>(smpServers.values());
    }


    @Override
    public Optional<SMPServer> getServer(String name) {
        return Optional.ofNullable(smpServers.getOrDefault(name, null));
    }

    @Override
    public boolean canStartServer(){
        return smpServers.values().stream().filter(SMPServer::isEnabled).count() < configManager.config().maxServersAmount;
    }
}
