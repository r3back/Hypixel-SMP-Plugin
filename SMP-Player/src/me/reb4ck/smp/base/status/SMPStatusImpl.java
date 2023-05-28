package me.reb4ck.smp.base.status;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.status.SMPStatus;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.SMPMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.SMPGetter;
import me.reb4ck.smp.utils.ServerGetter;

import java.util.UUID;

@Singleton
public final class SMPStatusImpl implements SMPStatus, ServerGetter, SMPGetter {
    private final String server;
    private final BoxUtil boxUtil;
    private SMPServer smpServer;

    @Inject
    public SMPStatusImpl(BoxUtil boxUtil) {
        this.server = getServerName(boxUtil.plugin);
        this.boxUtil = boxUtil;
    }

    @Override
    public void powerOn() {
        boxUtil.scheduler.runTaskAsynchronously(boxUtil.plugin, () -> {
            smpServer = getServer(UUID.randomUUID(), server, boxUtil);

            if(smpServer == null) return;

            smpServer.setEnabled(true);

            boxUtil.redisService.publishUpdate(smpServer, SMPMessage.ActionType.UPDATE);
        });
    }

    @Override
    public void powerOff() {
        if(smpServer == null) return;

        smpServer.setEnabled(false);

        smpServer.setPlayers(0);

        String updateMessage = boxUtil.persist.toString(
                SMPMessage.builder()
                        .actionType(SMPMessage.ActionType.UPDATE)
                        .smpServer(boxUtil.persist.toString(smpServer, Persist.PersistType.JSON))
                        .build(),
                Persist.PersistType.JSON);


        boxUtil.redisService.publishSync(Channel.SMP_UPDATES.getName(), updateMessage);

        boxUtil.redisService.publishSync(Channel.REMOVE_FROM_BUNGEE.getName(), smpServer.getName());


    }
}
