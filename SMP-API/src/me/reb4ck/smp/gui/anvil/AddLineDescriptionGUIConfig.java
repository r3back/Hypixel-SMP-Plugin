package me.reb4ck.smp.gui.anvil;

import lombok.Builder;
import lombok.NoArgsConstructor;
import me.reb4ck.smp.utils.item.Item;

@NoArgsConstructor
@Builder
public final class AddLineDescriptionGUIConfig {
    public String title;
    public String startTitle;
    public Item rightItem;
    public Item leftItem;

    public AddLineDescriptionGUIConfig(String title, String startTitle, Item rightItem, Item leftItem) {
        this.title = title;
        this.startTitle = startTitle;
        this.rightItem = rightItem;
        this.leftItem = leftItem;
    }
}
