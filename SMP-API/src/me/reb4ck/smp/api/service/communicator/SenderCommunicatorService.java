package me.reb4ck.smp.api.service.communicator;

import me.reb4ck.smp.message.MessageReceiver;
import me.reb4ck.smp.server.SMPServer;

public interface SenderCommunicatorService {
    void createServer(SMPServer smpServer);

    void deleteServer(SMPServer smpServer);

    void startServer(SMPServer smpServer);

    void stopServer(SMPServer smpServer);

    void updateServer(SMPServer smpServer, int newRam);

    void publish(MessageReceiver channel, String message);

    void updateVirtualUsers();
}
