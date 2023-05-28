package me.reb4ck.smp.database.types;

import com.zaxxer.hikari.HikariConfig;

import java.io.File;
import java.io.IOException;

public final class SQLite extends SQL{
    private static final String FILE_NAME = "playerdata.db";
    private final String filePath;
    private final String name;

    public SQLite(String name, File file) {
        super();
        this.name = name;
        this.filePath = file.getPath() + File.separator + FILE_NAME;
        createDBFile();
    }

    public HikariConfig getDatabase(){

        final HikariConfig hikari = new HikariConfig();
        try {
            hikari.setPoolName("SMP-" + name +"-" + POOL_COUNTER.getAndIncrement());
            hikari.setDriverClassName("org.sqlite.SQLiteDataSource");
            hikari.setJdbcUrl("jdbc:sqlite:" + this.filePath);
            hikari.setConnectionTestQuery("SELECT 1");
            hikari.setMinimumIdle(MINIMUM_IDLE);
            hikari.setMaxLifetime(MAX_LIFETIME);
            hikari.setConnectionTimeout(CONNECTION_TIMEOUT);
            hikari.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
            hikari.setLeakDetectionThreshold(LEAK_DETECTION_THRESHOLD);
        }catch (Exception e){
            e.printStackTrace();
        }

        return hikari;
    }

    private void createDBFile() {
        File yourFile = new File(this.filePath);
        try {
            yourFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
