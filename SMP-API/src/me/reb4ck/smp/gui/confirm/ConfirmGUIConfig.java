package me.reb4ck.smp.gui.confirm;

import lombok.NoArgsConstructor;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.SimpleGUI;
import me.reb4ck.smp.utils.item.Item;

@NoArgsConstructor
public final class ConfirmGUIConfig extends SimpleGUI {
    public Item confirmItem;
    public Item cancelItem;

    public ConfirmGUIConfig(String title, int size, Background background, Item closeGUI, Item confirmItem, Item cancelItem) {
        super(title, size, background, closeGUI);
        this.confirmItem = confirmItem;
        this.cancelItem = cancelItem;
    }
}
