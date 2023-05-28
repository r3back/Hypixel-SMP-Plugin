package me.reb4ck.smp.gui;

import lombok.NoArgsConstructor;
import me.reb4ck.smp.utils.item.Item;

import java.util.Map;

/**
 * GUI Background
 */
@NoArgsConstructor
public final class Background {
    public Map<Integer, Item> items;

    /**
     * Default Constructor
     *
     * @param items BackGround Slots and Items
     */
    public Background(Map<Integer, Item> items) {
        this.items = items;
    }
}
