package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.factory.DatabaseFactory;
import me.reb4ck.smp.api.factory.OSFactory;
import me.reb4ck.smp.api.service.communicator.ReceiverCommunicatorService;
import me.reb4ck.smp.api.service.OSService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.database.Database;
import me.reb4ck.smp.utils.LinuxTerminal;
import me.reb4ck.smp.message.SMPMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.server.SMPServerImpl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Singleton
public final class BKCommunicatorServiceImpl implements ReceiverCommunicatorService, LinuxTerminal {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    private final BukkitScheduler taskScheduler;
    private final OSService osService;
    private final JavaPlugin plugin;
    private final Database database;
    private final Persist persist;

    @Inject
    public BKCommunicatorServiceImpl(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, JavaPlugin plugin, BukkitScheduler taskScheduler, OSFactory osFactory, Persist persist,
                                     DatabaseFactory databaseFactory) {
        this.osService = osFactory.getOSService();
        this.configManager = configManager;
        this.taskScheduler = taskScheduler;
        this.database = databaseFactory.getDatabase();
        this.plugin = plugin;
        this.persist = persist;
    }

    @Override
    public void handle(SMPMessage smpMessage) {
        SMPServer server = getObject(SMPServerImpl.class, smpMessage.getSmpServer());

        switch (smpMessage.getActionType()){
            case CREATE:
                createServer(server);
                break;
            case DELETE:
                deleteServer(server);
                break;
            case START:
                startServer(server);
                break;
            case STOP:
                stopServer(server);
                break;
            case UPDATE:
                updateServer(server, smpMessage.getNewRam());
                break;
            case UPDATE_VIRTUAL:
                updateVirtualUsers();
                break;
            case TELEPORT:
                break;
        }
    }

    public void createServer(SMPServer smpServer) {
        taskScheduler.runTaskAsynchronously(plugin, () -> {
            osService.createDocker(smpServer, smpServer.getPort());

            taskScheduler.runTaskLaterAsynchronously(plugin, () -> {
                //Create A user Directory inside VSFTPD Server
                executeCommand("docker exec -i vsftpd mkdir /home/vsftpd/" +  smpServer.getName());

                //Creates property file and also copy all files inside docker
                createPropertiesFile(smpServer);

                osService.granPermissionsToFile(smpServer);

                //Add the user to the VSFTPD Users
                updateVirtualUsers();

                //Start the server
                if(smpServer.isEnabled())
                    osService.startServer(smpServer);
            }, 60);

        });

    }

    public void deleteServer(SMPServer smpServer) {
        osService.deleteDocker(smpServer.getName());
        osService.deleteFTPFolder(smpServer);
    }

    public void startServer(SMPServer smpServer) {
        osService.startDocker(smpServer);
        osService.startServer(smpServer);
        osService.granPermissionsToFile(smpServer);
    }


    private String getParsedPath(String path){
        return path.replace("%plugin_folder%", plugin.getDataFolder().getAbsolutePath().toString());
    }

    public void updateVirtualUsers(){
        CompletableFuture<List<SMPServer>> future = new CompletableFuture<>();

        taskScheduler.runTaskAsynchronously(plugin, () -> future.complete(database.getServers()));

        future.thenAccept(servers -> {

            String path = getParsedPath(configManager.config().defaultUsersFolder);
            try {
                File folder = new File(path);

                if(!folder.exists())
                    folder.mkdir();

                String pathTxt = path + "/virtual_users.txt";

                String dbPath = path + "/virtual_users.db";

                final File txtFile = new File(pathTxt);

                final File dbFile = new File(dbPath);

                if(!txtFile.exists()) txtFile.createNewFile();

                if(!dbFile.exists()) dbFile.createNewFile();

                PrintWriter writer = new PrintWriter(txtFile, "UTF-8");

                writer.flush();

                for(SMPServer smpServer : servers){
                    writer.println(smpServer.getName());
                    writer.println(smpServer.getPassword());
                }

                writer.close();

                String command = "/usr/bin/db_load -T -t hash -f "+path+" "+dbPath;

                executeCommand(command);

                taskScheduler.runTaskAsynchronously(plugin, () -> {
                    String destine = "/etc/vsftpd/";

                    osService.copyFilesToContainer("vsftpd", path + "/" , destine);

                    osService.restartDocker("vsftpd");
                });


                taskScheduler.runTaskLaterAsynchronously(plugin, () -> Optional.ofNullable(folder).ifPresent(File::delete), 400);
            } catch (IOException e) {
                System.err.println("Failed to create virtual users file!" + e.getMessage());
            }
        });

    }

    public void stopServer(SMPServer smpServer){
        //osService.stopDocker(smpServer);
    }

    public void updateServer(SMPServer smpServer, int newRam) {
        try {
            String source = getParsedPath(configManager.config().temporalFolder);

            File init = new File(source);

            if(!init.exists()) init.mkdir();

            String UUId = UUID.randomUUID().toString();

            String folder = source + "/"+ UUId +"/";

            String pathTxt = folder + osService.getStartFile();

            final File file = new File(pathTxt);

            final File folderF = new File(folder);

            if(!folderF.exists()) folderF.mkdir();

            if(!file.exists()){
                file.createNewFile();

                PrintWriter writer = new PrintWriter(file, "UTF-8");

                configManager.config().linuxStartContent
                        .forEach(line -> writer.println(line.replace("%ram%", String.valueOf(newRam))));

                writer.close();
            }

            executeCommand("chmod 100 " + pathTxt);

            osService.copyShFile(smpServer.getName(), pathTxt);

            taskScheduler.runTaskLaterAsynchronously(plugin, () -> {
                Optional.ofNullable(file).ifPresent(File::delete);
                Optional.ofNullable(folderF).ifPresent(File::delete);
            }, 50 * 20);

        } catch (IOException e) {
            System.err.println("Failed to create prop file!" + e.getMessage());
        }
    }

    private void createPropertiesFile(SMPServer smpServer){
        try {
            String sourceFr = getParsedPath(configManager.config().defaultServerFolder) + "/.";

            String source = getParsedPath(configManager.config().temporalFolder);

            File init = new File(source);

            if(!init.exists())
                init.mkdir();

            String UUId = UUID.randomUUID().toString();

            String folder = source + "/"+ UUId +"/";

            String toPaste = source + "/"+ UUId;

            String pathTxt = folder + "server.properties";

            final File file = new File(pathTxt);

            final File folderF = new File(folder);

            if(!folderF.exists())
                folderF.mkdir();

            if(!file.exists()){
                file.createNewFile();

                PrintWriter writer = new PrintWriter(file, "UTF-8");

                configManager.config().defaultServerProperties.forEach(writer::println);
                writer.println("server-port="+smpServer.getPort());
                writer.println("server-name="+smpServer.getName());

                writer.close();
            }

            osService.copyAllFilesToTemp(sourceFr, toPaste);

            osService.copyAllFilesToDocker(smpServer, folder + ".");

            taskScheduler.runTaskLaterAsynchronously(plugin, () -> {
                Optional.ofNullable(file).ifPresent(File::delete);
                Optional.ofNullable(folderF).ifPresent(File::delete);
            }, 200);

        } catch (IOException e) {
            System.err.println("Failed to create prop file!" + e.getMessage());
        }
    }

    protected  <T> T getObject(Class<T> clazz, String str){
        return str == null || str.equals("") ? null : persist.load(clazz, str, Persist.PersistType.JSON);
    }
}
