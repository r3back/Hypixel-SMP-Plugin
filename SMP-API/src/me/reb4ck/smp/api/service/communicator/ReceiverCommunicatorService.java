package me.reb4ck.smp.api.service.communicator;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.base.service.BKCommunicatorServiceImpl;
import me.reb4ck.smp.message.SMPMessage;

@ImplementedBy(BKCommunicatorServiceImpl.class)
public interface ReceiverCommunicatorService {
    void handle(SMPMessage smpMessage);
}
