package me.reb4ck.smp.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import me.reb4ck.smp.api.registry.CommandRegistry;
import me.reb4ck.smp.base.JacksonPersist;
import me.reb4ck.smp.base.hooks.LuckPermsPermissions;
import me.reb4ck.smp.commands.provider.CommandProvider;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class InitialModule extends AbstractModule {
    @Override
    public void configure(){

        bind(new TypeLiteral<PermissionsPlugin<Player>>() {}).to(LuckPermsPermissions.class).asEagerSingleton();

        bind(new TypeLiteral<CommandRegistry<CoreCommand>>() {}).to(CommandProvider.class).asEagerSingleton();

        bind(Persist.class).to(JacksonPersist.class).asEagerSingleton();

        bind(new TypeLiteral<List<SMPServer>>() {}).annotatedWith(Names.named("servers")).toInstance(new ArrayList<>());

        bind(new TypeLiteral<List<String>>() {}).annotatedWith(Names.named("blocked")).toInstance(new ArrayList<>());

    }
}
