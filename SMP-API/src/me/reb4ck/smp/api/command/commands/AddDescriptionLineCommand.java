package me.reb4ck.smp.api.command.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.service.DatabaseService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.config.*;
import me.reb4ck.smp.base.reactive.ReactiveServiceImpl;
import me.reb4ck.smp.base.server.SMPServerImpl;
import me.reb4ck.smp.message.FutureMessage;
import me.reb4ck.smp.message.GUIMessage;
import me.reb4ck.smp.utils.Console;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.api.gui.GUIList;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.api.tracker.SMPRestAPI;
import me.reb4ck.smp.api.tracker.ITrackID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class AddDescriptionLineCommand extends CoreCommand {
    private final BoxUtil boxUtil;

    @Inject
    public AddDescriptionLineCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().addDescriptionLineCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getServer(player, name, trackID.getValue(), boxUtil);

            //Optional<SMPServer> smpServer = service.getServer(name).filter(server -> server.getUuid().equals(trackID.getValue()));

            if(server != null) {
                SerializedGUI gui = SerializedGUI.builder()
                        .strings(new HashMap<String, String>(){{
                            put(SerializedGUI.Value.SERVER_NAME.getValue(), name);
                        }})
                        .type(GUIList.ADD_DESCRIPTION_LINE)
                        .build();

                String toSend = boxUtil.persist.toString(gui, Persist.PersistType.JSON);

                boxUtil.redisService.publish(Channel.GUIS.getName(), GUIMessage.builder()
                        .player(player.getName())
                        .gui(toSend)
                        .build());
            }
        }else if(args.length >= 3) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getServer(player, name, trackID.getValue(), boxUtil);

            if(server != null) {

                String line = getDesc(args);

                if(line.length() > 20){
                    player.sendMessage(StringUtils.color(configManager.messages().exceedDescriptionLineLimit));
                    return;
                }

                if(server.getDescription().size() >= 8){
                    player.sendMessage(StringUtils.color(configManager.messages().exceedDescriptionLimit));
                    return;
                }

                server.getDescription().add(line);

                player.sendMessage(StringUtils.color(configManager.messages().successfullyUpdatedDescription));

                boxUtil.redisService.publishUpdate(server);

                //databaseService.saveSMPServer(server);
            }else
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }



    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    private String getDesc(String[] args){
        StringBuilder str = new StringBuilder();

        for(int i = 2 ; i < args.length; i++)
            str
                    .append(args[i].replace("%empty%", " "))
                    .append(i == args.length - 1 ? "" : " ");

        return str.toString();
    }
}
