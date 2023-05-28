package me.reb4ck.smp.api.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TableConfigDatabase extends ConfigDatabase{
    public String table = "tokens";

    @JsonIgnore
    public TableConfigDatabase(String host, String database, String userName, String passWord, int port, boolean ssl, String type, String table) {
        super(host, database, userName, passWord, port, ssl, type);
        this.table = table;
    }
}
