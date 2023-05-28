package me.reb4ck.smp.utils;

import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.placeholder.IPlaceholder;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.base.placeholder.Placeholder;

import java.util.Arrays;
import java.util.List;

public final class PlaceholderUtils {
    private List<IPlaceholder> getInfo(SerializedGUI serializedGUI, ConfigManager<Configuration, Messages, Commands, Inventories> configManager){
        boolean status = serializedGUI.getBoolean(SerializedGUI.Value.SERVER_STATUS);

        String message = configManager.messages().serverStatusPlaceholder.get(status);

        String smpName = serializedGUI.getString(SerializedGUI.Value.SERVER_NAME);

        String smpDisplay = serializedGUI.getString(SerializedGUI.Value.SERVER_DISPLAY_NAME);

        int smpCurrentPlayers = serializedGUI.getInteger(SerializedGUI.Value.SERVER_CURRENT_PLAYERS);

        int smpMaxPlayers = serializedGUI.getInteger(SerializedGUI.Value.SERVER_MAX_PLAYERS);

        return Arrays.asList(
                new Placeholder("server_status", message),
                new Placeholder("server_name", smpName),
                new Placeholder("server_display_name", smpDisplay),
                new Placeholder("server_players", smpCurrentPlayers),
                new Placeholder("server_max_players", smpMaxPlayers)
        );
    }
}
