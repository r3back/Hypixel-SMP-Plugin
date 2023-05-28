package me.reb4ck.smp.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import me.reb4ck.smp.api.factory.DatabaseFactory;
import me.reb4ck.smp.database.future.FutureDatabase;
import me.reb4ck.smp.database.future.FutureDatabaseImpl;
import me.reb4ck.smp.database.handler.DBFavoritesHandler;
import me.reb4ck.smp.database.handler.DBHandler;
import me.reb4ck.smp.database.handler.DBServerHandler;
import me.reb4ck.smp.factory.DatabaseFactoryImpl;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class DatabaseModule extends AbstractModule {
    @Override
    public void configure(){

        bind(DatabaseFactory.class).to(DatabaseFactoryImpl.class).asEagerSingleton();

        bind(new TypeLiteral<DBHandler<Player>>() {}).to(DBFavoritesHandler.class).asEagerSingleton();

        bind(new TypeLiteral<DBHandler<SMPServer>>() {}).to(DBServerHandler.class).asEagerSingleton();

        bind(FutureDatabase.class).to(FutureDatabaseImpl.class).asEagerSingleton();

    }
}
