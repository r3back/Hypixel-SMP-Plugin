package me.reb4ck.smp.database.credentials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.reb4ck.smp.api.config.ConfigDatabase;
import me.reb4ck.smp.api.config.TableConfigDatabase;

@Getter
@AllArgsConstructor
public final class Credentials {

    private final String host, databaseName, userName, password;
    private final DatabaseType databaseType;
    private final boolean ssl;
    private final int port;
    private final String table;

    public static Credentials fromConfig(ConfigDatabase config) {
        String host = config.host;
        String dbName = config.database;
        String userName = config.userName;
        String password = config.passWord;
        String table = config instanceof TableConfigDatabase ? ((TableConfigDatabase) config).table : "";
        int port = config.port;
        boolean ssl = config.ssl;
        DatabaseType databaseType = DatabaseType.valueOf(config.type.toUpperCase());


        return new Credentials(host,dbName,userName,password,databaseType,ssl,port,table);
    }

    public enum DatabaseType {
        MYSQL,
        SQLITE
    }
}