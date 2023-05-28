package me.reb4ck.smp.gui.anvil;

import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.utils.InventoryUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public final class NameServerGUI {
    private final AnvilGUIConfig config;
    private final BoxUtil util;
    private String input;

    public NameServerGUI(BoxUtil util) {
        this.config = util.config.inventories().anvilGUIConfig;
        this.util = util;

    }

    public void openTo(Player p){
        util.scheduler.runTask(util.plugin, () -> new AnvilGUI.Builder()
                .onClose(player -> {
                    if(this.input != null)
                        player.performCommand("smp create " + input);
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
                .onRightInputClick(player -> {player.sendMessage("");})
                .title(config.title)
                .plugin(util.plugin)
                .open(p));
    }
}
