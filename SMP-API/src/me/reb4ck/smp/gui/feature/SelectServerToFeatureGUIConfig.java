package me.reb4ck.smp.gui.feature;

import lombok.NoArgsConstructor;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.admin_all.AdminAllServersGUIConfig;
import me.reb4ck.smp.utils.item.Item;

import java.util.List;

@NoArgsConstructor
public final class SelectServerToFeatureGUIConfig extends AdminAllServersGUIConfig {
    public Item goBack;

    public SelectServerToFeatureGUIConfig(String title, int size, Background background, Item closeGUI, Item displayItem, Item lockedItem, Item emptyItem, List<Integer> slots, Item goBack) {
        super(title, size, background, closeGUI, displayItem, lockedItem, emptyItem, slots);
        this.goBack = goBack;
    }
}
