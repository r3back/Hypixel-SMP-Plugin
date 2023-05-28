package me.reb4ck.smp.base.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ImmutableMap;
import lombok.NoArgsConstructor;
import me.reb4ck.smp.api.Timer;
import me.reb4ck.smp.api.config.ConfigDatabase;
import me.reb4ck.smp.api.config.ConfigFeature;
import me.reb4ck.smp.api.config.TableConfigDatabase;
import me.reb4ck.smp.api.ram.RamSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Configuration {

    public String prefix = "&e[SMP] ";

    public ConfigFeature configFeature = new ConfigFeature();
    public Timer shutdownUnlimitedServersAfter = new Timer(10, Timer.TimeType.SECONDS);
    public String unlimitedServerPermission = "smp.unlimited";

    public ConfigDatabase configDatabase = new ConfigDatabase();
    public TableConfigDatabase tokenManagerDatabase = new TableConfigDatabase();

    public String hostIp = "localhost";

    public String lobbyServer = "lobby";

    public int maxServersAmount = 5;

    public Integer serverPerPages = 43;

    public String redisUri = "redis://[USER]:[PASSWORD]@[SERVICE-IP]:[PORT]";

    public List<Integer> availablePorts = Arrays.asList(25569, 25570, 25571, 25572, 25573, 25574, 25575, 25576, 25577, 25578, 25579);

    public Integer initialRam = 2;

    public Integer interMaxAmountOfDiskInGb = 2;
    public Integer initialFavoriteSlots = 2;
    public Integer initialServerSlots = 1;

    public Integer maxTimeoutTime = 5;

    public RamSettings ramSettings = new RamSettings(ImmutableMap.<Integer, Integer>builder()
            .put(3, 2500)
            .put(4, 4500)
            .put(5, 6500)
            .put(6, 10500)
            .build());

    public List<String> blockedServers = Arrays.asList("blockedServer1", "blockedServer2", "root");

    public Map<String, Integer> favoritePerPermission = ImmutableMap.<String, Integer>builder()
            .put("smp.favorites.4", 4)
            .put("smp.favorites.5", 5)
            .put("smp.favorites.6", 6)
            .build();

    public Map<String, Integer> serverAmountPerPermission = ImmutableMap.<String, Integer>builder()
            .put("smp.server.4", 4)
            .put("smp.server.5", 5)
            .put("smp.server.6", 6)
            .build();

    public int maxFavorites = 10;

    @JsonIgnore
    public String privateRedisUri = "redis://:PASSWORD@IP:PORT";

    @JsonIgnore
    public ConfigDatabase privateConfigDatabase = new ConfigDatabase("localhost", "smp_db", "my_user", "my_password", 3306, false, "MYSQL");

    @JsonIgnore
    public TableConfigDatabase privateTokenManagerDatabase = new TableConfigDatabase("localhost", "smp_db", "my_user", "my_password", 3306, false, "MYSQL", "tokenmanager");

    public boolean smpServer = false;
    public String defaultServerFolder = "%plugin_folder%/../../SMPServers/defaultServer";
    public String temporalFolder = "%plugin_folder%/../../SMPServers/temp";
    public String defaultUsersFolder = "%plugin_folder%/../../SMPServers/virtualUsers";

    public String downloadSpigotLink = "https://api.papermc.io/v2/projects/paper/versions/1.18.2/builds/386/downloads/paper-1.18.2-386.jar";
    public boolean downloadSpigot = true;

    public boolean useCredentialsFromHere = false;

    public List<String> linuxStartContent = Arrays.asList("#!/bin/bash", "cd /home/server/", "java -Xmx%ram%G -Xms%ram%G -jar /home/server/spigot nogui");

    public List<String> defaultServerProperties = Arrays.asList(
            "enable-jmx-monitoring=false",
            "level-seed=",
            "gamemode=0",
            "enable-command-block=false",
            "enable-query=false",
            "generator-settings=",
            "level-name=world",
            "motd=A Minecraft Server",
            "pvp=true",
            "generate-structures=true",
            "difficulty=1",
            "network-compression-threshold=256",
            "max-tick-time=60000",
            "use-native-transport=true",
            "max-players=20",
            "online-mode=false",
            "enable-status=true",
            "allow-flight=false",
            "broadcast-rcon-to-ops=true",
            "view-distance=10",
            "max-build-height=256",
            "server-ip=",
            "allow-nether=true",
            "enable-rcon=false",
            "sync-chunk-writes=true",
            "op-permission-level=4",
            "prevent-proxy-connections=false",
            "resource-pack=",
            "entity-broadcast-range-percentage=100",
            "rcon.password=",
            "player-idle-timeout=0",
            "debug=false",
            "force-gamemode=false",
            "rate-limit=0",
            "hardcore=false",
            "white-list=false",
            "broadcast-console-to-ops=true",
            "spawn-npcs=true",
            "spawn-animals=true",
            "snooper-enabled=true",
            "function-permission-level=2",
            "level-type=default",
            "text-filtering-config=",
            "spawn-monsters=true",
            "enforce-whitelist=false",
            "resource-pack-sha1=",
            "spawn-protection=16",
            "max-world-size=29999984");

    public List<String> protectedFiles = Arrays.asList(
            "start.sh",
            "spigot",
            "spigot.jar",
            "spigot.yml",
            "eula.txt",

            "plugins/SMPBukkit",
            "plugins/SMPBukkit/configuration.yml",
            "plugins/SMPBukkit/inventories.yml",
            "plugins/SMPBukkit/messages.yml",
            "plugins/SMPBukkit-Player-1.0.jar",

            "plugins/TokenManager",
            "plugins/TokenManager/config.yml",
            "plugins/TokenManager/data.yml",
            "plugins/TokenManager/lang.yml",
            "plugins/TokenManager/shops.yml",
            "plugins/TokenManager/worth.yml",
            "plugins/TokenManager-3.2.5.jar"
    );
}
