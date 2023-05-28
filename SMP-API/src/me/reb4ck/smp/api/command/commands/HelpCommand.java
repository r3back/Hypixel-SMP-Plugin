package me.reb4ck.smp.api.command.commands;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.impl.CoreCommand;
import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.registry.CommandRegistry;
import me.reb4ck.smp.base.config.*;
import me.reb4ck.smp.utils.StringUtils;
import me.reb4ck.smp.api.hooks.PermissionsPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class HelpCommand extends CoreCommand {
    private final CommandRegistry<CoreCommand> commands;

    @Inject
    public HelpCommand(ConfigManager<Configuration, Messages, Commands, Inventories> configManager, CommandRegistry<CoreCommand> commands) {
        super(configManager.commands().helpCommand, configManager);
        this.commands = commands;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args.length >= 1) {
            sendHelp(player);
        }else
            sender.sendMessage(StringUtils.color(configManager.messages().invalidArguments.replace("%prefix%", configManager.config().prefix)));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }


    private void sendHelp(Player p, String... args){
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            }catch (NumberFormatException e){
                return;
            }
        }
        List<CoreCommand> commandList = commands.getCommands().stream().filter(command -> !command.isExcludedCommand()).collect(Collectors.toList());

        int maxpage = (int) Math.ceil(commandList.size() / 18.00);
        int current = 0;
        p.sendMessage(StringUtils.color(configManager.messages().helpHeader));
        for (CoreCommand command : commandList) {
            if ((p.hasPermission(command.permission) || command.permission.equalsIgnoreCase("") || command.permission.equalsIgnoreCase("smp.")) && command.enabled) {
                if (current >= (page - 1) * 18 && current < page * 18)
                    p.sendMessage(StringUtils.color(configManager.messages().helpMessage.replace("%usage%", command.syntax).replace("%description%", command.description)));
                current++;
            }
        }
        BaseComponent[] components = TextComponent.fromLegacyText(StringUtils.color(configManager.messages().helpfooter.replace("%max_page%", maxpage + "").replace("%page%", page + "")));

        for (BaseComponent component : components) {
            if (ChatColor.stripColor(component.toLegacyText()).contains(configManager.messages().nextPage)) {
                if (page < maxpage) {
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/smp help " + (page + 1)));
                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(configManager.messages().helpPageHoverMessage.replace("%page%", "" + (page + 1))).create()));
                }
            } else if (ChatColor.stripColor(component.toLegacyText()).contains(configManager.messages().previousPage)) {
                if (page > 1) {
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/smp help " + (page - 1)));
                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(configManager.messages().helpPageHoverMessage.replace("%page%", "" + (page - 1))).create()));
                }
            }
        }
        p.spigot().sendMessage(components);
    }
}
