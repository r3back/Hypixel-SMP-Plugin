package me.reb4ck.smp.api.command.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.service.DatabaseService;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.api.tracker.SMPRestAPI;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class RemoveDescriptionLineCommand extends CoreCommand {
    private final BoxUtil boxUtil;

    @Inject
    public RemoveDescriptionLineCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().removeDescriptionLineCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = getServer(player, name, trackID.getValue(), boxUtil);

            if(server != null) {
                List<String> desc = server.getDescription();

                if(desc.size() != 0) {
                    String toRemove = desc.get(desc.size() - 1);

                    List<String> newDesc = desc.stream().filter(line -> !line.equals(toRemove)).collect(Collectors.toList());

                    server.setDescription(newDesc);
                }else{
                    player.sendMessage(StringUtils.color(configManager.messages().descriptionIsEmpty));
                    return;
                }

                player.sendMessage(StringUtils.color(configManager.messages().successfullyRemovedLine));

                boxUtil.redisService.publishUpdate(server);
            }else
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
