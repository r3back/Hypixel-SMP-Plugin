package me.reb4ck.smp.api.handler;

import com.google.inject.ImplementedBy;
import me.reb4ck.smp.api.gui.GUIList;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.handler.GUIHandlerImpl;
import me.reb4ck.smp.message.GUIMessage;
import org.bukkit.entity.Player;

@ImplementedBy(GUIHandlerImpl.class)
public interface GUIHandler {
    void open(Player player, GUIMessage message, BoxUtil util);
}
