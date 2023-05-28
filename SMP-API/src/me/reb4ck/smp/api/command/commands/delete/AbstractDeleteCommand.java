package me.reb4ck.smp.api.command.commands.delete;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.gui.GUIList;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.message.GUIMessage;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractDeleteCommand  extends CoreCommand {
    protected final BoxUtil boxUtil;

    @Inject
    public AbstractDeleteCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().deleteCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1) {
            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServers(trackID.getValue()).stream()
                    .findFirst()
                    .orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            delete(player, server.getName(), true);
        }else if(args.length == 2) {
            delete(player, args[1], true);
        }else if(args.length == 3 && args[2].equalsIgnoreCase(SerializedGUI.Value.DELETE_SERVER_CODE.getValue())){
            delete(player, args[1], false);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    protected void delete(Player player, String name, boolean sendConfirmGui){
        ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

        SMPServer server = getServer(player, name, trackID.getValue(), boxUtil);

        if (server == null) {
            player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));
            return;
        }

        if(sendConfirmGui){
            sendMenu(player, name);
            return;
        }

        player.sendMessage(StringUtils.color(configManager.messages().deletingServer.replace("%server%", name)));

        boxUtil.tpService.teleportAllPlayers(server, configManager.config().lobbyServer)
                .thenRun(() -> {

                    player.sendMessage(StringUtils.color(configManager.messages().successfullyDeleted.replace("%server%", name)));

                    boxUtil.smpService.deleteServer(server);
                });
    }

    private void sendMenu(Player player, String name){

        Map<String, String> strings = ImmutableMap.<String, String>builder()
                .put(SerializedGUI.Value.SERVER_NAME.getValue(), name)
                .build();

        SerializedGUI gui = SerializedGUI.builder()
                .type(GUIList.CONFIRM_DELETE)
                .strings(strings)
                .build();

        String toSend = boxUtil.persist.toString(gui, Persist.PersistType.JSON);

        boxUtil.redisService.publish(Channel.GUIS.getName(), GUIMessage.builder()
                .player(player.getName())
                .gui(toSend)
                .build());
    }
}
