package me.reb4ck.smp.database.executions;

import com.zaxxer.hikari.HikariDataSource;
import me.reb4ck.smp.database.credentials.Credentials;
import me.reb4ck.smp.database.types.MySQL;
import me.reb4ck.smp.database.types.SQLite;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DatabaseExecution {
    protected final HikariDataSource smpConnection;
    protected final HikariDataSource tmConnection;

    protected final File file;

    protected final String smpServersTableName = "SMP_Servers";
    protected final String settlementsColName = "NAME";

    protected final String smpFavoritesTableName = "SMP_Favorites";
    protected final String smpFavoritesColName = "NAME";

    protected final String tokenManagerTable;

    public DatabaseExecution(File file, Credentials smpCredentials, Credentials tmCredentials, String name) {
        this.file = file;
        this.tokenManagerTable = tmCredentials.getTable();
        this.smpConnection = getDatabase(smpCredentials, name);
        this.tmConnection = getDatabase(tmCredentials, name);
    }

    protected synchronized void execute(String sql, Object... replacements) {
        try (Connection c = this.smpConnection.getConnection(); PreparedStatement statement = c.prepareStatement(sql)) {
            if (replacements != null){
                for (int i = 0; i < replacements.length; i++){
                    statement.setObject(i + 1, replacements[i]);
                }
            }
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected HikariDataSource getDatabase(Credentials credentials, String name){
        if(credentials == null) return null;

        if(credentials.getDatabaseType() == Credentials.DatabaseType.MYSQL) {
            return new HikariDataSource(new MySQL().getDatabase(credentials));
        }else{
            return new HikariDataSource(new SQLite(name, file).getDatabase());
        }

    }

    public void closeHikari(){
        smpConnection.close();
    }
}
