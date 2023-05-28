package me.reb4ck.smp.gui.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.reb4ck.smp.gui.Background;
import me.reb4ck.smp.gui.SimpleGUI;
import me.reb4ck.smp.utils.item.Item;

@Getter
@Setter
@NoArgsConstructor
public final class IndividualServerGUIConfig extends SimpleGUI {
    public Item deleteServer;
    public Item stopServer;
    public Item startServer;
    public Item teleportServer;
    public Item infoServer;
    public Item backButton;
    public Item ramUpgrade;
    public Item wikiItem;
    public Item descriptionItem;
    public Item opCommand;

    public IndividualServerGUIConfig(String title, int size, Background background, Item closeGUI, Item deleteServer, Item stopServer, Item startServer, Item teleportServer, Item descriptionItem,
                                     Item infoServer, Item backButton, Item ramUpgrade, Item wikiItem, Item opCommand) {
        super(title, size, background, closeGUI);
        this.deleteServer = deleteServer;
        this.stopServer = stopServer;
        this.startServer = startServer;
        this.teleportServer = teleportServer;
        this.infoServer = infoServer;
        this.backButton = backButton;
        this.wikiItem = wikiItem;
        this.ramUpgrade = ramUpgrade;
        this.descriptionItem = descriptionItem;
        this.opCommand = opCommand;

    }
}
