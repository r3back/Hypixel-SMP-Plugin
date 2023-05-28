package me.reb4ck.smp.api.command.commands.setlogo;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.tracker.ITrackID;
import me.reb4ck.smp.api.utils.BoxUtil;
import me.reb4ck.smp.server.SMPLogo;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractSetLogoCommand extends CoreCommand {
    protected final BoxUtil boxUtil;

    @Inject
    public AbstractSetLogoCommand(BoxUtil boxUtil) {
        super(boxUtil.config.commands().setLogoCommand, boxUtil.config);
        this.boxUtil = boxUtil;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 4) {
            String name = args[1];

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServer(name).filter(server1 -> server1.getUuid().equals(trackID.getValue())).orElse(null);

            setLogo(player, name, server, true, args);
        }else if(args.length == 3) {

            ITrackID trackID = boxUtil.smpRestAPI.getTrackId(player.getName());

            SMPServer server = boxUtil.smpService.getServers(trackID.getValue()).stream().findFirst().orElse(null);

            if(server == null){
                player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServer));
                return;
            }

            setLogo(player, server.getName(), server, false, args);

        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }

    protected void setLogo(Player player, String name, SMPServer server, boolean withName, String... args){
        if(server != null) {
            Optional<SMPLogo> smpLogo = getSMPLogo(player, withName, args);

            if(!smpLogo.isPresent())
                return;

            server.setLogo(smpLogo.get());

            player.sendMessage(StringUtils.color(configManager.messages().successfullyUpdatedLogo));

            boxUtil.redisService.publishUpdate(server);
        }else
            player.sendMessage(StringUtils.color(configManager.messages().youDontHaveServerWithThatName.replace("%server%", name)));

    }

    private Optional<SMPLogo> getSMPLogo(Player player, boolean withName, String... args){
        int toRest = withName ? 0 : 1;

        String material = args[2 - toRest].toUpperCase();

        boolean glow;

        try{
            glow = Boolean.parseBoolean(args[3 - toRest]);
        }catch (Exception e){
            player.sendMessage(StringUtils.color(configManager.messages().glowMessage));
            return Optional.empty();
        }

        return Optional.of(new SMPLogo(material, glow));
    }

}
