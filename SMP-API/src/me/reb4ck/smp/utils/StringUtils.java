package me.reb4ck.smp.utils;

import me.reb4ck.smp.api.config.ConfigManager;
import me.reb4ck.smp.api.utils.message.MessageBuilder;
import me.reb4ck.smp.api.utils.message.MultipleSpecialMessage;
import me.reb4ck.smp.api.utils.message.SpecialMessage;
import me.reb4ck.smp.base.config.Commands;
import me.reb4ck.smp.base.config.Configuration;
import me.reb4ck.smp.base.config.Inventories;
import me.reb4ck.smp.api.placeholder.IPlaceholder;
import me.reb4ck.smp.base.config.Messages;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class StringUtils {

    /**
     * Applies colors to the provided string.
     * Supports hex colors in Minecraft 1.16+.
     *
     * @param string The string which should have colors
     * @return The new String with applied colors
     */
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Applies colors to the provided strings.
     * Supports hex colors in Minecraft 1.16+.
     * The order of the elements in the list will be the same.
     *
     * @param strings The strings which should have colors
     * @return A list of the same strings with colors
     */
    public static List<String> color(List<String> strings) {
        return strings.stream().map(StringUtils::color).collect(Collectors.toList());
    }

    /**
     * Applies placeholders to the provided strings.
     * Also adds colors.
     * The order of the Strings will be the same.
     *
     * @param lines        The lines which potentially have placeholders
     * @param placeholders The placeholders which should be replaced
     * @return The same lines with replaced placeholders
     */
    public static List<String> processMultiplePlaceholders(List<String> lines, List<IPlaceholder> placeholders) {
        return lines.stream().map(s -> processMultiplePlaceholders(s, placeholders)).collect(Collectors.toList());
    }

    /**
     * Applies Placeholders to the provided string.
     * Also adds colors.
     *
     * @param line         The line which potentially has placeholders
     * @param placeholders The placeholders which should be replaced
     * @return The same line with replaced placeholders
     */
    public static String processMultiplePlaceholders(String line, List<IPlaceholder> placeholders) {
        String processedLine = line;
        for (IPlaceholder placeholder : placeholders)
            processedLine = placeholder.process(processedLine);
        return color(processedLine);
    }

    public static String doubleToStr(double value){
        return String.valueOf((int) value);
    }

    public static void sendMessage(Player player, SpecialMessage specialMessage, List<IPlaceholder> placeholders){
        List<String> messages = specialMessage.message.stream().map(message -> processMultiplePlaceholders(message, placeholders)).collect(Collectors.toList());
        String action = processMultiplePlaceholders(specialMessage.action, placeholders);
        String aboveMessage = processMultiplePlaceholders(specialMessage.aboveMessage, placeholders);

        messages.forEach(message -> player.spigot().sendMessage(MessageBuilder.get(message, action, aboveMessage)));
    }

    public static TextComponent getMessage(MultipleSpecialMessage multipleSpecialMessage, List<IPlaceholder> placeholders){
        List<TextComponent> textComponents = new ArrayList<>();
        for(SpecialMessage specialMessage : multipleSpecialMessage.specialMessages){
            List<String> messages = specialMessage.message.stream().map(message -> processMultiplePlaceholders(message, placeholders)).collect(Collectors.toList());
            String action = processMultiplePlaceholders(specialMessage.action, placeholders);
            String aboveMessage = processMultiplePlaceholders(specialMessage.aboveMessage, placeholders);
            messages.stream().map(message -> MessageBuilder.get(message, action, aboveMessage)).forEach(textComponents::add);
        }
        TextComponent textComponent = new TextComponent();
        textComponents.forEach(textComponent::addExtra);
        return textComponent;
    }

    public static String getTimedMessage(String str, long millis){
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return str
                .replace("%seconds%", String.valueOf(seconds))
                .replace("%hours%", String.valueOf(hours))
                .replace("%minutes%", String.valueOf(minutes))
                .replace("%days%", String.valueOf(days));
    }

    public static String getTimedMessage(String str, long millis, ConfigManager<Configuration, Messages, Commands, Inventories> configManager){
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return str
                .replace("%seconds%", seconds > 0 ? configManager.messages().seconds.replace("%seconds%", String.valueOf(seconds)) : "")
                .replace("%hours%", hours > 0 ? configManager.messages().hours.replace("%hours%", String.valueOf(hours)) : "")
                .replace("%minutes%", minutes > 0 ? configManager.messages().minutes.replace("%minutes%", String.valueOf(minutes)) : "")
                .replace("%days%", days > 0 ? configManager.messages().days.replace("%days%", String.valueOf(days)) : "");
    }
}
