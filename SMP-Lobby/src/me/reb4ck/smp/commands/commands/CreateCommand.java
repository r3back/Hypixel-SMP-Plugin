package me.reb4ck.smp.commands.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.exception.port.NoPortAvailableException;
import me.reb4ck.smp.api.service.SMPService;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.service.TeleportService;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.base.config.Messages;
import me.reb4ck.smp.message.GUIMessage;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.channel.Channel;
import me.reb4ck.smp.api.gui.GUIList;
import me.reb4ck.smp.api.gui.SerializedGUI;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import me.reb4ck.smp.persist.Persist;
import me.reb4ck.smp.api.placeholder.IPlaceholder;
import me.reb4ck.smp.redis.RedisService;
import me.reb4ck.smp.server.SMPServer;
import me.reb4ck.smp.base.placeholder.Placeholder;
import me.reb4ck.smp.api.tracker.SMPRestAPI;
import me.reb4ck.smp.api.tracker.ITrackID;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

public final class CreateCommand extends CoreCommand {
    private final Persist persist;
    private final SMPRestAPI smpRestAPI;
    private final SMPService serverService;
    private final RedisService communicator;
    private final TeleportService teleportService;
    private final PermissionsPlugin<Player> permissionsPlugin;

    @Inject
    public CreateCommand(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, SMPService serverService, TeleportService teleportService, Persist persist,
                         PermissionsPlugin<Player> permissionsPlugin, SMPRestAPI smpRestAPI, RedisService communicator) {
        super(configManager.commands().createCommand, configManager);

        this.persist = persist;
        this.smpRestAPI = smpRestAPI;
        this.communicator = communicator;
        this.serverService = serverService;
        this.teleportService = teleportService;
        this.permissionsPlugin = permissionsPlugin;
    }



    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length == 2) {

            String name = args[1].toLowerCase();

            Optional<SMPServer> toCheck = serverService.getServer(name);

            if (toCheck.isPresent() || configManager.config().blockedServers.contains(name)) {
                player.sendMessage(StringUtils.color(configManager.messages().nameInUse.replace("%server%", name)));
                return;
            }

            if(name.length() > 20){
                player.sendMessage(StringUtils.color(configManager.messages().exceedNameLimit));
                return;
            }

            if(!Pattern.matches("[a-zA-Z]+", name)){
                player.sendMessage(StringUtils.color(configManager.messages().specialCharactersNotAllowed.replace("%server%", name)));
                return;
            }

            try {
                ITrackID iTrackID = smpRestAPI.getTrackId(player.getName());

                int serverAmount = (int) serverService.getServers()
                        .stream()
                        .filter(smpServer -> smpServer.getUuid().equals(iTrackID.getValue()))
                        .count();

                int playerSlots = permissionsPlugin.getServerSlots(player.getName());

                if(serverAmount >= playerSlots){
                    player.sendMessage(StringUtils.color(configManager.messages().youDontHaveEmptySlots));
                    return;
                }

                boolean canStart = serverService.canStartServer();

                SMPServer smpServer = serverService.addServer(player.getName(), iTrackID.getValue(), name, canStart);

                teleportService.teleport(player, smpServer, 20);

                player.sendMessage(StringUtils.color(configManager.messages().successfullyCreated.replace("%server%", name)));

                List<IPlaceholder> placeholders = Arrays.asList(new Placeholder("username", smpServer.getName()), new Placeholder("password", smpServer.getPassword()));

                configManager.messages().ftpData.forEach(message -> player.sendMessage(StringUtils.processMultiplePlaceholders(message, placeholders)));

                sendPassWord(player, smpServer);

                if(!canStart)
                    player.sendMessage(StringUtils.color(configManager.messages().serverLimitReach));

            } catch (NoPortAvailableException e) {
                player.sendMessage(StringUtils.color(configManager.messages().noPortsAvailable));
            }
        }else if(args.length == 1){
            SerializedGUI gui = SerializedGUI.builder()
                    .type(GUIList.ANVIL_GUI)
                    .build();

            String toSend = persist.toString(gui, Persist.PersistType.JSON);

            communicator.publish(Channel.GUIS.getName(), GUIMessage.builder()
                    .gui(toSend)
                    .player(player.getName())
                    .build());
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    private void sendPassWord(Player player, SMPServer smpServer){
        String message = configManager.messages().ftpPassword.action.replace("%password%", smpServer.getPassword());

        TextComponent textComponent = new TextComponent(StringUtils.color(message));

        if (message != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, smpServer.getPassword()));
        }
        if (message != null) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringUtils.color(configManager.messages().ftpPassword.aboveMessage)).create()));
        }
        player.spigot().sendMessage(textComponent);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
