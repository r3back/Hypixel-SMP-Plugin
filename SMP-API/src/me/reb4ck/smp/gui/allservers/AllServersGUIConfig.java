package me.reb4ck.smp.gui.allservers;

import lombok.NoArgsConstructor;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.SimpleGUI;
import me.reb4ck.smp.utils.item.Item;

@NoArgsConstructor
public final class AllServersGUIConfig extends SimpleGUI {
    public Item backPage;
    public Item nextPage;
    public Item goBack;
    public Item serverItem;

    public AllServersGUIConfig(String title, int size, Background background, Item closeGUI, Item backPage, Item nextPage, Item goBack, Item serverItem) {
        super(title, size, background, closeGUI);
        this.backPage = backPage;
        this.nextPage = nextPage;
        this.goBack = goBack;
        this.serverItem = serverItem;
    }
}
