package me.reb4ck.smp.api.service;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.base.service.DockerServiceImpl;
import me.reb4ck.smp.server.SMPServer;

@ImplementedBy(DockerServiceImpl.class)
public interface DockerService {
    void createDocker(SMPServer smpServer);

    void startDocker(SMPServer smpServer);

    void stopDocker(SMPServer smpServer);

    void copyFilesToContainer(String container, String source, String destine);

    void deleteDocker(String smpServer);

    void restartDocker(String container);

    boolean containerExists(String name);

    boolean isRunning(String name);


    void close();
}
