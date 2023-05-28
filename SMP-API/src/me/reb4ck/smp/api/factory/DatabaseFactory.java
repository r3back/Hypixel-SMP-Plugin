package me.reb4ck.smp.api.factory;

import me.reb4ck.smp.database.Database;

@FunctionalInterface
public interface DatabaseFactory {
    /**
     *
     * @return Database
     */
    Database getDatabase();
}
