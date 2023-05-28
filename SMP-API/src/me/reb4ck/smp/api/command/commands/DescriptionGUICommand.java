package me.reb4ck.smp.api.command.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.message.GUIMessage;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.api.gui.GUIList;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.api.tracker.ITrackID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public final class DescriptionGUICommand extends CoreCommand {
    private final BoxUtil boxUtil;

    @Inject
    public DescriptionGUICommand(BoxUtil boxUtil){
        super(boxUtil.config.commands().descriptionCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 2) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getServer(player, name, trackID.getValue(), boxUtil);

            Optional.ofNullable(server).ifPresent(s -> open(player, s.getName()));

        } else if (args.length == 1) {
            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            Optional<SMPServer> server = getAllPlayerServers(trackID.getValue(), boxUtil).stream()
                    .findFirst();

            if (!server.isPresent()) {
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            String name = server.get().getName();

            open(player, name);

        } else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));

    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    private void open(Player player, String name){
        SerializedGUI gui = SerializedGUI.builder()
                .strings(new HashMap<String, String>() {{
                    put(SerializedGUI.Value.SERVER_NAME.getValue(), name);
                }})
                .type(GUIList.DESCRIPTION)
                .build();

        String toSend = boxUtil.persist.toString(gui, Persist.PersistType.JSON);

        boxUtil.redisService.publish(Channel.GUIS.getName(), GUIMessage.builder()
                .player(player.getName())
                .gui(toSend)
                .build());
    }
}
