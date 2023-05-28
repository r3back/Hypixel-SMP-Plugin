package me.reb4ck.smp.base.service.communicator;

import com.google.inject.Inject;
import me.reb4ck.smp.api.service.communicator.SenderCommunicatorService;
import me.reb4ck.smp.base.server.SMPUpgrade;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.MessageReceiver;
import me.reb4ck.smp.message.SMPMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.Bukkit;

public final class SenderCommunicatorServiceImpl implements SenderCommunicatorService {
    private final RedisService redisService;
    private final Persist persist;

    @Inject
    public SenderCommunicatorServiceImpl(Persist persist, RedisService redisService) {
        this.redisService = redisService;
        this.persist = persist;
    }

    @Override
    public void createServer(SMPServer smpServer) {
        //Add to cache in all servers
        publish(Channel.SMP_UPDATES.getName(), SMPMessage.builder()
                .smpServer(persist.toString(smpServer, Persist.PersistType.JSON))
                .actionType(SMPMessage.ActionType.CREATE)
                .build());

        Bukkit.getConsoleSender().sendMessage("Sent Create Signal SENDER Communicator Service");
        publish(getMessage(smpServer, SMPMessage.ActionType.CREATE));
    }

    @Override
    public void deleteServer(SMPServer smpServer) {
        //Delete from cache in all servers
        publish(Channel.SMP_UPDATES.getName(), SMPMessage.builder()
                .smpServer(persist.toString(smpServer, Persist.PersistType.JSON))
                .actionType(SMPMessage.ActionType.DELETE)
                .build());

        publish(getMessage(smpServer, SMPMessage.ActionType.DELETE));
    }

    @Override
    public void startServer(SMPServer smpServer) {
        publish(getMessage(smpServer, SMPMessage.ActionType.START));
    }

    @Override
    public void stopServer(SMPServer smpServer) {
        publish(getMessage(smpServer, SMPMessage.ActionType.STOP));
    }

    @Override
    public void updateServer(SMPServer smpServer, int newRam) {
        publish(getMessage(smpServer, SMPMessage.ActionType.UPDATE, newRam));

        smpServer.setSmpUpgrade(new SMPUpgrade(smpServer.getRam(), newRam));

        smpServer.setRam(newRam);
    }

    @Override
    public void publish(MessageReceiver channel, String message) {
        redisService.publish(channel.getName(), message);
    }

    private void publish(String channel, SMPMessage message){
        redisService.publish(channel, persist.toString(message, Persist.PersistType.JSON));
    }

    private void publish(String message){
        redisService.publish(MessageReceiver.RECEIVER.getName(), message);
    }

    @Override
    public void updateVirtualUsers() {
        publish(getMessage(null, SMPMessage.ActionType.UPDATE_VIRTUAL));
    }


    private String getMessage(SMPServer smpServer, SMPMessage.ActionType actionType, Integer... integers){
        String server = smpServer == null ? "" : persist.toString(smpServer, Persist.PersistType.JSON);

        int newRam = integers == null || integers.length <= 0 ? 0 : integers[0];

        SMPMessage smpMessage = SMPMessage.builder()
                .smpServer(server)
                .actionType(actionType)
                .newRam(newRam)
                .build();

        return persist.toString(smpMessage, Persist.PersistType.JSON);
    }
}
