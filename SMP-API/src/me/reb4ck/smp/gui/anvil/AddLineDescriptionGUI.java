package me.reb4ck.smp.gui.anvil;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.utils.InventoryUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public final class AddLineDescriptionGUI {
    private final AddLineDescriptionGUIConfig config;
    private final BoxUtil util;
    private final String name;
    private String input;

    public AddLineDescriptionGUI(BoxUtil util, String name) {
        this.config = util.config.inventories().addLineDescriptionGUIConfig;
        this.util = util;
        this.name = name;
    }

    public void openTo(Player p){
        util.scheduler.runTask(util.plugin, () -> new AnvilGUI.Builder()
                .onClose(player -> {
                    if (this.input != null)
                        player.performCommand("smp adddescriptionline " + name + " " + input);
                })
                .onComplete((player, text) -> {
                    input = text;
                    return AnvilGUI.Response.close();
                })
                .text(config.startTitle)
                .itemLeft(InventoryUtils.makeItem(config.leftItem))
                .itemRight(InventoryUtils.makeItem(config.rightItem))
                .onLeftInputClick(player -> {
                    player.sendMessage("");
                })
                .onRightInputClick(player -> {
                    player.sendMessage("");
                })
                .title(config.title)
                .plugin(util.plugin)
                .open(p));
    }
}
