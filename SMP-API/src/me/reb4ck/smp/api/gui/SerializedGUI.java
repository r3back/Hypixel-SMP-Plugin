package me.reb4ck.smp.api.gui;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class SerializedGUI {
    private GUIList type;
    private Map<String, Boolean> booleans;
    private Map<String, String> strings;
    private Map<String, Double> doubles;
    private Map<String, Integer> integers;
    private Map<String, List<String>> stringList;

    @JsonIgnore
    public String getString(String key){
        return strings.getOrDefault(key, "");
    }

    @JsonIgnore
    public Integer getInteger(String key){
        return integers.getOrDefault(key, 0);
    }

    @JsonIgnore
    public Double getDouble(String key){
        return doubles.getOrDefault(key, 0D);
    }

    @JsonIgnore
    public Boolean getBoolean(String key){
        return booleans.getOrDefault(key, false);
    }

    @JsonIgnore
    public List<String> getList(String key){
        return stringList.getOrDefault(key, new ArrayList<>());
    }

    //
    @JsonIgnore
    public String getString(Value key){
        return getString(key.getValue());
    }

    @JsonIgnore
    public Integer getInteger(Value key){
        return getInteger(key.getValue());
    }

    @JsonIgnore
    public Double getDouble(Value key){
        return getDouble(key.getValue());
    }

    @JsonIgnore
    public Boolean getBoolean(Value key){
        return getBoolean(key.getValue());
    }

    @JsonIgnore
    public List<String> getList(Value key){
        return getList(key.getValue());
    }

    @AllArgsConstructor
    @Getter
    public enum Value{
        DELETE_SERVER_CODE("44SD4A54D2A2S2D1Sa._"),
        DELETE_ALL_SERVER_CODE("secretkey766"),

        //Server Placeholders
        SERVER_STATUS("status"),
        SERVER_DISPLAY_NAME("smpDisplay"),
        SERVER_CURRENT_PLAYERS("smpCurrentPlayers"),
        SERVER_MAX_PLAYERS("smpMaxPlayers"),

        SERVER_RAM("smpRam"),
        SERVER_USER("smpUser"),
        SERVER_PASSWORD("smpPassword"),
        SERVER_RAM_AMOUNT("smpRamAmount_"),

        SERVER_OWNER_NAME("smpOwnerName"),
        SERVER_DESCRIPTION("smpDescription"),
        SERVER_NAME("smpName"),

        TOKEN_MANAGER("tokenManager"),
        SERVERS_PER_PAGE("serversPerPage"),

        FAVORITE_SLOTS("favoriteSlots"),
        SERVER_SLOTS("serverSlots"),

        //SMP MENU
        ALL_SERVERS("all_servers"),
        TRACK_ID("track_id"),


        ;

        private final String value;
    }
}
