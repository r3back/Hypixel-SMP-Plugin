package me.reb4ck.smp.api.config;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.base.config.implementation.ConfigManagerImpl;

/**
 * Implementation to handle config files
 */
@ImplementedBy(ConfigManagerImpl.class)
public interface ConfigManager<C, M, CM, I> {
    C config();
    M messages();
    I inventories();
    CM commands();

    /**
     * Reload Config Files
     */
    void reloadFiles();

    /**
     * Save Config Files
     */
    void saveFiles();
}