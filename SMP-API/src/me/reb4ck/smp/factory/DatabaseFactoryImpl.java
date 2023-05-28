package me.reb4ck.smp.factory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.factory.DatabaseFactory;
import me.reb4ck.smp.database.SQLDatabase;
import me.reb4ck.smp.database.Database;

@Singleton
public final class DatabaseFactoryImpl implements DatabaseFactory {
    private final Injector injector;
    private final Database database;

    @Inject
    public DatabaseFactoryImpl(Injector injector) {
        this.injector = injector;
        this.database = setupDatabase();
    }

    @Override
    public Database getDatabase(){
        return database;
    }

    private Database setupDatabase(){
        return injector.getInstance(SQLDatabase.class);
    }
}
