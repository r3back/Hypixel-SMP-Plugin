package me.reb4ck.smp.base.service.os;

import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.service.OSService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public abstract class OSServiceCommon implements OSService {
    protected static final String createCommand = "docker run -d -i -t --name %name% -p %port%:%port% -v /home/ftpdata/%name%:/home/server r3b4ck/paper-server";
    //protected static final String createCommand = "docker run -d -i -t --name %name% --storage-opt size=%disk_size%G -p %port%:%port% -v /home/ftpdata/%name%:/home/server r3b4ck/paper-server";


    protected static final String startServerCommand = "docker exec -i -d -t -w /home/server %name% java -Xmx%ram%G -Xms%ram%G -Duser.timezone=GMT+2 -jar /home/server/spigot nogui";
    protected static final String deleteFolderCommand = "docker exec -i -d -t -w /home/vsftpd vsftpd rm -r %name%";
    protected static final String grantPermissionsCommand =
            "docker exec -i -d -t vsftpd chmod 000 /home/vsftpd/%name%/%file_name%";

    protected static final String copyShFileCommand = "docker cp %source% vsftpd:/home/vsftpd/%name%/";
    protected static final String copyFilesCommand = "docker cp %source%/. %name%:%destine%/";
    protected static final String copyPropCommand = "docker cp %source% %name%:%destine%";
    protected static final String copyAll = "cp -r %source% %destine%";

    protected final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    protected final JavaPlugin plugin;

    public OSServiceCommon(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, JavaPlugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    @Override
    public void createStartFile(String path, int ram) {
        //try {
            //final File file = new File(path + "/" + getStartFile());

            //file.createNewFile();

            //PrintWriter writer = new PrintWriter(file, "UTF-8");

            //List<String> content = configManager.config().linuxStartContent;

            //content.forEach(line -> writer.println(line.replace("%ram%", String.valueOf(ram))));

            //writer.close();

            //granPermissionsToFile(path);

        //} catch (IOException e) {
        //    System.err.println("Failed to create sh file!" + e.getMessage());
        //}
    }

    protected String getParsedPath(String path){
        return path.replace("%plugin_folder%", plugin.getDataFolder().getAbsolutePath().toString());
    }}
