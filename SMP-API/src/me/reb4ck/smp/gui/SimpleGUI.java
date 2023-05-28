package me.reb4ck.smp.gui;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.reb4ck.smp.utils.item.Item;

@AllArgsConstructor
@NoArgsConstructor
public class SimpleGUI {
    public String title;
    public int size;
    public Background background;
    public Item closeGUI;
}
