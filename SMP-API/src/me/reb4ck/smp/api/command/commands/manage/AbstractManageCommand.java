package me.reb4ck.smp.api.command.commands.manage;

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

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractManageCommand extends CoreCommand {
    protected final BoxUtil boxUtil;

    @Inject
    public AbstractManageCommand(BoxUtil boxUtil){
        super(boxUtil.config.commands().manageCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            String name = args[1];

            double money = boxUtil.addonsService.getEconomy().getMoney(player.getName());

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServer(name).filter(server1 ->  server1.getUuid().equals(trackID.getValue())).orElse(null);

            manage(player, name, money, server);
        }else if(args.length == 1){
                String name = null;

                double money = boxUtil.addonsService.getEconomy().getMoney(player.getName());

                ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

                SMPServer server = boxUtil.smpService.getServers(trackID.getValue()).stream()
                        .findFirst()
                        .orElse(null);

                if(server == null){
                    player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                    return;
                }

                manage(player, name, money, server);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    protected void manage(Player player, String name, double money, SMPServer server){

        if(server == null){
            player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));
            return;
        }

        Map<String, String> strings = ImmutableMap.<String, String>builder()
                .put(SerializedGUI.Value.SERVER_NAME.getValue(), server.getName())
                .put(SerializedGUI.Value.SERVER_USER.getValue(), server.getName())
                .put(SerializedGUI.Value.SERVER_PASSWORD.getValue(), server.getPassword())
                .put(SerializedGUI.Value.SERVER_DISPLAY_NAME.getValue(), server.getName())
                .build();

        Map<String, Integer> ramAndPrice = new HashMap<String, Integer>(){{
            put(SerializedGUI.Value.SERVER_CURRENT_PLAYERS.getValue(), server.getPlayers());
            put(SerializedGUI.Value.SERVER_MAX_PLAYERS.getValue(), server.getMaxPlayers());
            put(SerializedGUI.Value.SERVER_RAM.getValue(), server.getRam());
        }};

        for(Integer amount : boxUtil.reactiveService.getRamPrices().getPrices().keySet())
            ramAndPrice.put(SerializedGUI.Value.SERVER_RAM_AMOUNT + String.valueOf(amount) , boxUtil.reactiveService.getRamPrices().getPrices().get(amount));

        Map<String, Boolean> booleans = ImmutableMap.<String, Boolean>builder()
                .put(SerializedGUI.Value.SERVER_STATUS.getValue(), server.isEnabled())
                .build();


        SerializedGUI gui = SerializedGUI.builder()
                .type(GUIList.ADMIN_INDIVIDUAL_SERVER)

                .strings(strings)
                .doubles(new HashMap<String, Double>(){{
                    put(SerializedGUI.Value.TOKEN_MANAGER.getValue(), money);
                }})
                .booleans(booleans)
                .integers(ramAndPrice)
                .build();

        String toSend = boxUtil.persist.toString(gui, Persist.PersistType.JSON);

        boxUtil.redisService.publish(Channel.GUIS.getName(), GUIMessage.builder()
                .gui(toSend)
                .player(player.getName())
                .build());
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
