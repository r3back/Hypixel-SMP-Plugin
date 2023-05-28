package me.reb4ck.smp.gui.description;

import lombok.NoArgsConstructor;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.SimpleGUI;
import me.reb4ck.smp.utils.item.Item;

@NoArgsConstructor
public final class DescriptionGUIConfig extends SimpleGUI {
    public Item resetDescriptionItem;
    public Item currentDescription;
    public Item removeLineItem;
    public Item addLineItem;
    public Item goBack;

    public DescriptionGUIConfig(String title, int size, Background background, Item closeGUI, Item resetDescriptionItem, Item removeLineItem, Item addLineItem, Item currentDescription, Item goBack) {
        super(title, size, background, closeGUI);
        this.resetDescriptionItem = resetDescriptionItem;
        this.currentDescription = currentDescription;
        this.removeLineItem = removeLineItem;
        this.addLineItem = addLineItem;
        this.goBack = goBack;
    }
}
