package me.reb4ck.smp.api.utils.message;

import me.reb4ck.smp.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public final class MessageBuilder {
    public static TextComponent get(String message) {
        return new TextComponent(StringUtils.color(message));
    }

    public static TextComponent get(String message, String url) {
        TextComponent textComponent = new TextComponent(StringUtils.color(message));
        if (url != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringUtils.color("&bClick to open " + url)).create()));
        }
        return textComponent;
    }

    public static TextComponent get(String message, String command, String hover) {
        TextComponent textComponent = new TextComponent(StringUtils.color(message));

        if (command != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }
        if (hover != null) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringUtils.color(hover)).create()));
        }
        return textComponent;
    }
}
