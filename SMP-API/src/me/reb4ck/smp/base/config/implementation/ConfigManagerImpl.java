package me.reb4ck.smp.base.config.implementation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.persist.Persist;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public final class ConfigManagerImpl implements ConfigManager<Configuration, Messages, Commands, Inventories> {
    private final Persist.PersistType persistType;
    private Configuration configuration;
    private Inventories inventories;
    private final Persist persist;
    private Messages messages;
    private Commands commands;

    @Inject
    public ConfigManagerImpl(JavaPlugin plugin, Persist persist){
        this.persist = persist;
        this.persistType = Persist.PersistType.YAML;
        loadFiles(plugin);
    }

    @Override
    public Configuration config() {
        return configuration;
    }

    @Override
    public Messages messages() {
        return messages;
    }

    @Override
    public Inventories inventories() {
        return inventories;
    }

    @Override
    public Commands commands() {
        return commands;
    }

    @Override
    public void reloadFiles() {
        configuration = persist.load(Configuration.class, persistType);
        messages = persist.load(Messages.class, persistType);
        commands = persist.load(Commands.class, persistType);
        inventories = persist.load(Inventories.class, persistType);
    }

    @Override
    public void saveFiles() {
        persist.save(configuration, persistType);
        persist.save(messages, persistType);
        persist.save(commands, persistType);
        persist.save(inventories, persistType);
    }

    private void loadFiles(JavaPlugin plugin){
        plugin.getDataFolder().mkdir();
        this.reloadFiles();
        this.saveFiles();
    }
}
