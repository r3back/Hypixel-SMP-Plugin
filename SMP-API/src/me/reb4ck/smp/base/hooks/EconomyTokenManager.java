package me.reb4ck.smp.base.hooks;

import com.google.inject.Inject;
import me.reb4ck.smp.api.factory.DatabaseFactory;
import me.reb4ck.smp.api.hooks.EconomyPlugin;
import me.reb4ck.smp.database.Database;

public final class EconomyTokenManager implements EconomyPlugin {
    private final Database database;

    @Inject
    public EconomyTokenManager(DatabaseFactory database) {
        this.database = database.getDatabase();
    }

    @Override
    public double getMoney(String player) {
        return this.database.getTokens(player);
    }
}
