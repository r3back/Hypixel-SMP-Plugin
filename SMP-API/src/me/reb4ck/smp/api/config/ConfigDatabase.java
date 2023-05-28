package me.reb4ck.smp.api.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ConfigDatabase {
    public String host = "localhost";
    public String database = "smp_test";
    public String userName = "root";
    public String passWord = "";
    public int port = 3306;
    public boolean ssl = false;
    public String type = "MYSQL";
}
