package me.reb4ck.smp.api.hooks;

public interface PermissionsPlugin<T>{
    boolean hasPermission(T player, String permission);

    boolean hasDefaultPermission(T player, String permission);

    boolean hasDefaultPermission(String name, String permission);

    int getFavoriteSlots(String name);

    int getServerSlots(String name);
}
