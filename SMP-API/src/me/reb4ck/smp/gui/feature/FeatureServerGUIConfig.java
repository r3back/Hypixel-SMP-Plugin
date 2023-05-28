package me.reb4ck.smp.gui.feature;

import lombok.NoArgsConstructor;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.SimpleGUI;
import me.reb4ck.smp.utils.item.Item;

@NoArgsConstructor
public final class FeatureServerGUIConfig extends SimpleGUI {
    public Item confirm;
    public Item goBack;

    public FeatureServerGUIConfig(String title, int size, Background background, Item closeGUI, Item confirm, Item goBack) {
        super(title, size, background, closeGUI);
        this.confirm = confirm;
        this.goBack = goBack;
    }
}
