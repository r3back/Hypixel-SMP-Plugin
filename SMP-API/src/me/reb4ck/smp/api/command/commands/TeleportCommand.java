package me.reb4ck.smp.api.command.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.server.SMPServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class TeleportCommand extends CoreCommand {
    private final BoxUtil boxUtil;

    @Inject
    public TeleportCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().teleportCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            String name = args[1];

            SMPServer server = getServer(player, name, boxUtil);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));
                return;
            }

            player.sendMessage(StringUtils.color(configManager.messages().teleporting));

            boxUtil.tpService.teleport(player, server, 0)
                    /*.handle((reply, e) -> {
                        if(e != null)
                            player.sendMessage(StringUtils.color(configManager.messages().yourServerIsOffStartFirst.replace("%server%", name)));
                        return null;
                    })*/;

        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
