package me.reb4ck.smp.gui.ram;

import lombok.NoArgsConstructor;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.SimpleGUI;
import me.reb4ck.smp.utils.item.Item;

import java.util.List;

@NoArgsConstructor
public final class RamUpgradeGUIConfig extends SimpleGUI {
    public List<RamItem> ramAndItems;
    public Item currentRamItem;
    public Item goBack;

    public RamUpgradeGUIConfig(String title, int size, Background background, Item closeGUI, List<RamItem> ramAndItems, Item currentRamItem, Item goBack) {
        super(title, size, background, closeGUI);
        this.currentRamItem = currentRamItem;
        this.ramAndItems = ramAndItems;
        this.goBack = goBack;
    }
}
