package me.reb4ck.smp.database.types;

import com.zaxxer.hikari.HikariConfig;
import me.reb4ck.smp.database.credentials.Credentials;

import java.util.HashMap;
import java.util.Map;

public final class MySQL extends SQL{
    public MySQL() {
    }

    public HikariConfig getDatabase(Credentials credentials){
        final HikariConfig hikari = new HikariConfig();
        Map<String, String> properties = new HashMap<>();

        // https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        properties.putIfAbsent("cachePrepStmts", "true");
        properties.putIfAbsent("prepStmtCacheSize", "250");
        properties.putIfAbsent("prepStmtCacheSqlLimit", "2048");
        properties.putIfAbsent("useServerPrepStmts", "true");
        properties.putIfAbsent("useLocalSessionState", "true");
        properties.putIfAbsent("rewriteBatchedStatements", "true");
        properties.putIfAbsent("cacheResultSetMetadata", "true");
        properties.putIfAbsent("cacheServerConfiguration", "true");
        properties.putIfAbsent("elideSetAutoCommits", "true");
        properties.putIfAbsent("maintainTimeStats", "false");
        properties.putIfAbsent("alwaysSendSetIsolation", "false");
        properties.putIfAbsent("cacheCallableStmts", "true");

        // https://stackoverflow.com/a/54256150
        // It's not super important which timezone we pick, because we don't use time-based
        // data types in any of our schemas/queries.
        properties.putIfAbsent("serverTimezone", "UTC");

        for (Map.Entry<String, String> property : properties.entrySet()) {
            hikari.addDataSourceProperty(property.getKey(), property.getValue());
        }

        hikari.setPoolName("SMP-" + POOL_COUNTER.getAndIncrement());

        hikari.setJdbcUrl("jdbc:mysql://" + credentials.getHost() + ":" + credentials.getPort() + "/" + credentials.getDatabaseName() + "?useSSL="+credentials.isSsl());

        hikari.setConnectionTestQuery("SELECT 1");
        hikari.setUsername(credentials.getUserName());
        hikari.setPassword(credentials.getPassword());

        hikari.setMinimumIdle(MINIMUM_IDLE);
        hikari.setMaxLifetime(MAX_LIFETIME);
        hikari.setConnectionTimeout(CONNECTION_TIMEOUT);
        hikari.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        hikari.setLeakDetectionThreshold(LEAK_DETECTION_THRESHOLD);

        hikari.setInitializationFailTimeout(-1);

        return hikari;
    }
}
