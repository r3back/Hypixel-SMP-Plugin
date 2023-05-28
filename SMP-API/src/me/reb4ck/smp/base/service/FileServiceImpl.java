package me.reb4ck.smp.base.service;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.factory.OSFactory;
import me.reb4ck.smp.api.service.FileService;
import me.reb4ck.smp.api.service.OSService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.utils.LinuxTerminal;
import me.reb4ck.smp.message.MessageReceiver;
import me.reb4ck.smp.utils.ServerGetter;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public final class FileServiceImpl implements FileService, LinuxTerminal, ServerGetter {
    private final ConfigManager<Configuration, Messages, Commands, Inventories> configManager;
    private final OSService osService;
    private final JavaPlugin plugin;
    private final String server;

    @Inject
    public FileServiceImpl(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, JavaPlugin plugin, OSFactory osFactory) {
        this.osService = osFactory.getOSService();
        this.server = getServerName(plugin);
        this.configManager = configManager;
        this.plugin = plugin;
    }


    private String getParsedPath(String path){
        return path.replace("%plugin_folder%", plugin.getDataFolder().getAbsolutePath().toString());
    }

    private void createEula(String path){
        try {
            final File file = new File(path + "/eula.txt");
            if(!file.exists()){
                file.createNewFile();
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                writer.println("eula=true");
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to create eula file!" + e.getMessage());
        }
    }

    @Override
    public void prepareDefaultServer(){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if(!server.equals(MessageReceiver.RECEIVER.getName())) return;

            String path = getParsedPath(configManager.config().defaultServerFolder);

            downloadSpigot(path);

            createPluginsFolder(path);

            createEula(path);

            createSpigotYaml(path);

            osService.createStartFile(path, 2);
        });

    }

    @Override
    public void disable() {
        if(!server.equals(MessageReceiver.RECEIVER.getName())) return;

        try {
            FileUtils.forceDelete(new File(configManager.config().temporalFolder));

        }catch (Exception e){
            System.err.println("Failed to delete temp folder!");
        }
    }

    private void downloadSpigot(String path){
        if(!configManager.config().downloadSpigot) return;

        osService.downloadFile(path, "spigot", configManager.config().downloadSpigotLink);
    }

    private void createPluginsFolder(String pt){
        try {
            String pathTxt = pt + "/plugins";

            Path path = Paths.get(pathTxt);

            Files.createDirectories(path);
        }catch (Exception e){
            System.err.println("Failed to create plugins folder!" + e.getMessage());
        }
    }

    private void createSpigotYaml(String path){
        try {
            String pathTxt = path + "/spigot.yml";

            File file = new File(pathTxt);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            InputStream inputStream = plugin.getResource("spigot.yml");

            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(reader);

            if (!file.exists()) {
                config.options().header(loadConfiguration.options().header());
                config.addDefaults(loadConfiguration);
                config.options().copyDefaults(true);
                config.save(file);
            }
        } catch (IOException e) {
            System.err.println("Failed to create spigot.yml file!" + e.getMessage());
        }
    }
}
