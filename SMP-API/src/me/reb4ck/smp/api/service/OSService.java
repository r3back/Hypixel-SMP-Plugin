package me.reb4ck.smp.api.service;

import me.reb4ck.smp.server.SMPServer;

public interface OSService {
    void granPermissionsToFile(SMPServer smpServer);

    void createStartFile(String path, int ram);

    void createDocker(SMPServer smpServer, int port);

    void deleteDocker(String dockerName);

    void restartDocker(String dockerName);

    void copyFilesToDocker(SMPServer smpServer);

    void deleteFTPFolder(SMPServer smpServer);

    void copyFilesToContainer(String container, String source, String destine);

    void copyShFile(String name, String source);

    void copyAllFilesToTemp(String name, String source);

    void copyAllFilesToDocker(SMPServer smpServer, String nextPath);

    void startDocker(SMPServer smpServer);

    void stopDocker(SMPServer smpServer);

    void startServer(SMPServer smpServer);

    String getStartFile();

    void downloadFile(String path, String finalName, String link);

    boolean isLinux();
}
