package me.reb4ck.smp.base.service.os;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.service.DockerService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.utils.LinuxTerminal;

import me.reb4ck.smp.server.SMPServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class LinuxService extends OSServiceCommon implements LinuxTerminal {
    private final DockerService dockerService;

    @Inject
    public LinuxService(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, JavaPlugin plugin, DockerService dockerService) {
        super(configManager, plugin);
        this.dockerService = dockerService;
    }

    @Override
    public void copyFilesToDocker(SMPServer smpServer) {
        try {
            String source = getParsedPath(configManager.config().defaultServerFolder);

            String destination = "/home/vsftpd/" + smpServer.getName();

            String command = copyFilesCommand
                    .replaceAll("%name%", "vsftpd")
                    .replaceAll("%source%", source)
                    .replaceAll("%destine%", destination);

            executeCommand(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFTPFolder(SMPServer smpServer) {
        try {
            String command = deleteFolderCommand
                    .replaceAll("%name%", smpServer.getName());

            executeCommand(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void copyFilesToContainer(String container, String source, String destine) {
        dockerService.copyFilesToContainer(container, source, destine);
    }

    @Override
    public void copyShFile(String name, String source) {
        try {
            String command = copyShFileCommand
                    .replaceAll("%source%", source)
                    .replaceAll("%name%", name);


            Console.sendMessage("&bCommand for SH: " + command);

            executeCommand(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void copyAllFilesToTemp(String source, String destine) {

        String command = copyAll
                .replaceAll("%source%", source)
                .replaceAll("%destine%", destine);

        executeCommand(command);
    }

    @Override
    public void copyAllFilesToDocker(SMPServer smpServer, String nextPath) {

        String destination = "/home/vsftpd/"+smpServer.getName();

        //"docker cp %source% %name%:%destine%";
        String command = copyPropCommand
                .replaceAll("%name%", "vsftpd")
                .replaceAll("%source%", nextPath)
                .replaceAll("%destine%", destination);

        executeCommand(command);
    }

    @Override
    public void createDocker(SMPServer smpServer, int port) {
        try {
            String dockerName = smpServer.getName();

            String command = createCommand
                    .replaceAll("%disk_size%", Optional.ofNullable(configManager.config().interMaxAmountOfDiskInGb)
                            .map(String::valueOf)
                            .orElse("2"))
                    .replaceAll("%name%", dockerName)
                    .replaceAll("%port%", String.valueOf(smpServer.getPort()));

            executeCommand(command);

            Bukkit.getConsoleSender().sendMessage("Executed create command");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDocker(String dockerName) {
        try {
            dockerService.deleteDocker(dockerName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void restartDocker(String dockerName) {
        dockerService.restartDocker(dockerName);
    }

    @Override
    public void startDocker(SMPServer smpServer) {
        dockerService.startDocker(smpServer);
    }

    @Override
    public void stopDocker(SMPServer smpServer) {
        dockerService.stopDocker(smpServer);
    }

    @Override
    public void startServer(SMPServer smpServer) {
        try {
            String dockerName = smpServer.getName();

            String command = startServerCommand
                    .replaceAll("%ram%", String.valueOf(smpServer.getRam()))
                    .replaceAll("%name%", dockerName);
            executeCommand(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStartFile() {
        return "start.sh";
    }

    @Override
    public void downloadFile(String path, String finalName, String link) {
        String finalPath = path + "/" + finalName;

        executeCommand("wget -O " + finalPath + " " + link);
    }

    @Override
    public void granPermissionsToFile(SMPServer smpServer) {
        try {
            for(String file : configManager.config().protectedFiles){
                String command = grantPermissionsCommand
                        .replaceAll("%file_name%", file)
                        .replaceAll("%name%", smpServer.getName());

                executeCommand(command);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLinux() {
        return true;
    }

}
