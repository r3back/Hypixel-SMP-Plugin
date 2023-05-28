package me.reb4ck.smp.base.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.reb4ck.smp.api.handler.GUIHandler;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.gui.admin.IndividualServerGUI;
import me.reb4ck.smp.gui.admin_all.AdminAllServersGUI;
import me.reb4ck.smp.gui.anvil.AddLineDescriptionGUI;
import me.reb4ck.smp.gui.anvil.NameServerGUI;
import me.reb4ck.smp.gui.confirm.ConfirmGUI;
import me.reb4ck.smp.gui.description.DescriptionGUI;
import me.reb4ck.smp.gui.main.FutureMainMenuGUI;
import me.reb4ck.smp.gui.main.LobbyMainMenuGUI;
import me.reb4ck.smp.api.gui.GUIList;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.gui.SerializedGUI.Value;
import me.reb4ck.smp.message.GUIMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.utils.FavoriteGetter;
import org.bukkit.entity.Player;

@Singleton
public final class GUIHandlerImpl implements GUIHandler, FavoriteGetter {
    private final Persist.PersistType persistType = Persist.PersistType.JSON;

    @Inject
    public GUIHandlerImpl() {
    }

    @Override
    public void open(Player player, GUIMessage message, BoxUtil util) {
        SerializedGUI gui = util.persist.load(SerializedGUI.class, message.getGui(), persistType);

        for (GUIList guiList : GUIList.values()) {
            if (!guiList.getName().equals(gui.getType().getName())) continue;

            if (guiList.equals(GUIList.ADMIN_INDIVIDUAL_SERVER))
                new IndividualServerGUI(gui, util, gui.getDouble(Value.TOKEN_MANAGER)).openSync(player);
            else if (guiList.equals(GUIList.CONFIRM_DELETE))
                new ConfirmGUI(util, gui.getString(Value.SERVER_NAME)).openSync(player);
            else if (guiList.equals(GUIList.MAIN_MENU)) {
                new FutureMainMenuGUI(
                        util,
                        player.getName(),
                        gui,
                        false
                ).openFuture(player);
            } else if (guiList.equals(GUIList.ADMIN_ALL_SERVERS))
                new AdminAllServersGUI(util, player.getName(), gui.getInteger(Value.SERVER_SLOTS)).openFuture(player);
            else if (guiList.equals(GUIList.ANVIL_GUI))
                new NameServerGUI(util).openTo(player);
            else if (guiList.equals(GUIList.ADD_DESCRIPTION_LINE))
                new AddLineDescriptionGUI(util, gui.getString(Value.SERVER_NAME)).openTo(player);
            else if (guiList.equals(GUIList.DESCRIPTION))
                new DescriptionGUI(util, gui.getString(Value.SERVER_NAME)).openFuture(player);
            return;
        }
    }
}
