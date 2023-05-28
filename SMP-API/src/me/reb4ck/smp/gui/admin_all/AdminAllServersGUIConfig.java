package me.reb4ck.smp.gui.admin_all;

import lombok.NoArgsConstructor;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.SimpleGUI;
import me.reb4ck.smp.utils.item.Item;

import java.util.*;

@NoArgsConstructor
public class AdminAllServersGUIConfig extends SimpleGUI {
    public Item displayItem;
    public Item lockedItem;
    public Item emptyItem;
    public List<Integer> slots;

    public AdminAllServersGUIConfig(String title, int size, Background background, Item closeGUI, Item displayItem, Item lockedItem, Item emptyItem, List<Integer> slots) {
        super(title, size, background, closeGUI);
        this.displayItem = displayItem;
        this.lockedItem = lockedItem;
        this.emptyItem = emptyItem;
        this.slots = slots;
    }
}
