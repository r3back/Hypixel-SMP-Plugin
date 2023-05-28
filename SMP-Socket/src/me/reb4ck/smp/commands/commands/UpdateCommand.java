package me.reb4ck.smp.commands.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.CommandConfig;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.factory.DatabaseFactory;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.database.Database;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.utils.LinuxTerminal;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public final class UpdateCommand extends CoreCommand implements LinuxTerminal {
    private final Database database;
    private final JavaPlugin plugin;
    private final BukkitScheduler scheduler;

    @Inject
    public UpdateCommand(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, DatabaseFactory factory, JavaPlugin plugin, BukkitScheduler scheduler) {
        super(new CommandConfig(Collections.singletonList("update"), "Update files", "/SMP update", "smp.update", false, Duration.ZERO, true), configManager);
        this.plugin = plugin;
        this.scheduler = scheduler;
        this.database = factory.getDatabase();
    }

    private String getParsedPath(String path){
        return path.replace("%plugin_folder%", plugin.getDataFolder().getAbsolutePath().toString());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 1){

            String source = getParsedPath("%plugin_folder%/../../SMPServers/updates/.");

            int i = 0;

            for(SMPServer server : database.getServers()){

                String destine = "/home/ftpdata/"+server.getName()+"/plugins/";

                executeCommand("cp -r " + source + " " + destine);

                i++;
            }

            Console.sendMessage("&aSuccessfully updated &2" + i + "&a servers!");

        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
