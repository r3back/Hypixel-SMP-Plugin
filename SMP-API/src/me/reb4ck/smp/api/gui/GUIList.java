package me.reb4ck.smp.api.gui;

import lombok.Getter;

public enum GUIList {
    MAIN_MENU("main_menu"),
    ALL_SERVERS("all_servers"),
    ADMIN_INDIVIDUAL_SERVER("manage_server"),
    CONFIRM_DELETE("confirm_delete"),
    ANVIL_GUI("anvil_gui"),
    ADD_DESCRIPTION_LINE("description_line_gui"),
    DESCRIPTION("description_gui"),
    ADMIN_ALL_SERVERS("admin_all_servers");

    @Getter
    private final String name;

    GUIList(String name){
        this.name = name;
    }
}
