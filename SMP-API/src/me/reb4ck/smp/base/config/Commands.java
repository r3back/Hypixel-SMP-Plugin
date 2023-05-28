package me.reb4ck.smp.base.config;

import me.reb4ck.smp.api.command.CommandConfig;

import java.time.Duration;
import java.util.Collections;

public final class Commands {
    public CommandConfig adminCommand = new CommandConfig(Collections.singletonList("admin"), "View all your created servers", "/SMP admin", "", true, Duration.ofSeconds(3), true);
    public CommandConfig createCommand = new CommandConfig(Collections.singletonList("create"), "Create SMP Server", "/SMP create [<name>]", "smp.create", true, Duration.ofSeconds(3), true);
    public CommandConfig deleteCommand = new CommandConfig(Collections.singletonList("delete"), "Delete a Server", "/SMP delete <name>", "smp.delete", true, Duration.ofSeconds(3), true);
    public CommandConfig helpCommand = new CommandConfig(Collections.singletonList("help"), "View all commands", "/SMP help [<page>]", "smp.help", true, Duration.ofSeconds(3), true);
    public CommandConfig manageCommand = new CommandConfig(Collections.singletonList("manage"), "Manage Specific Server", "/SMP manage <server>", "", true, Duration.ofSeconds(3), true);
    public CommandConfig menuCommand = new CommandConfig(Collections.singletonList("menu"), "Open Main menu", "/SMP menu", "smp.menu", true, Duration.ofSeconds(3), true);
    public CommandConfig reloadCommand = new CommandConfig(Collections.singletonList("reload"), "Reload Plugin Files", "/SMP reload", "smp.reload", false, Duration.ofSeconds(3), true);
    public CommandConfig setDescriptionCommand = new CommandConfig(Collections.singletonList("setdescription"), "Change your server description", "/SMP setdescription <name> <description-args>", "smp.setdescription", true, Duration.ofSeconds(3), true);
    public CommandConfig setDisplayNameCommand = new CommandConfig(Collections.singletonList("setdisplayname"), "Change your server displayname", "/SMP setdisplayname <name> <displayname>", "smp.setdisplayname", true, Duration.ofSeconds(3), true);
    public CommandConfig setLogoCommand = new CommandConfig(Collections.singletonList("setlogo"), "Change your server logo", "/SMP setlogo <name> <material> <glow>", "smp.setlogo", true, Duration.ofSeconds(3), true);
    public CommandConfig setUnlimitedCommand = new CommandConfig(Collections.singletonList("setunlimited"), "Set Server unlimited to a player", "/SMP setunlimited", "smp.setunlimited", false, Duration.ofSeconds(3), true);
    public CommandConfig startCommand = new CommandConfig(Collections.singletonList("start"), "Start your server", "/SMP start <name>", "smp.start", true, Duration.ofSeconds(3), true);
    public CommandConfig stopCommand = new CommandConfig(Collections.singletonList("stop"), "Stop your server", "/SMP stop <name>", "smp.stop", true, Duration.ofSeconds(3), true);
    public CommandConfig teleportCommand = new CommandConfig(Collections.singletonList("teleport"), "Teleport to one of your servers", "/SMP teleport <name>", "smp.teleport", true, Duration.ofSeconds(3), true);
    public CommandConfig unsetUnlimitedCommand = new CommandConfig(Collections.singletonList("unsetunlimited"), "Un Set Server unlimited to a player", "/SMP unsetunlimited <player>", "smp.unsetunlimited", false, Duration.ofSeconds(3), true);
    public CommandConfig lobbyCommand = new CommandConfig(Collections.singletonList("lobby"), "Teleport to lobby", "/SMP lobby", "smp.lobby", true, Duration.ofSeconds(3), true);
    public CommandConfig descriptionCommand = new CommandConfig(Collections.singletonList("description"), "Open description menu", "/SMP description", "smp.description", true, Duration.ofSeconds(3), true);

    public CommandConfig addDescriptionLineCommand = new CommandConfig(Collections.singletonList("adddescriptionline"), "Add a description line use '%empty%' to add an empty line", "/SMP adddescriptionline <name> <line-args>", "smp.setdisplayname", true, Duration.ofSeconds(3), true);
    public CommandConfig removeDescriptionLineCommand = new CommandConfig(Collections.singletonList("removedescriptionline"), "Remove last description line", "/SMP removedescriptionline <name>", "smp.removedescriptionline", true, Duration.ofSeconds(3), true);
    public CommandConfig forceDeleteCommand = new CommandConfig(Collections.singletonList("forcedelete"), "Force to Delete a Server", "/SMP forcedelete <name>", "smp.forcedelete", true, Duration.ofSeconds(3), true);
    public CommandConfig resetDescriptionCommand = new CommandConfig(Collections.singletonList("resetdescription"), "Remove all description", "/SMP resetdescription <name>", "smp.resetdescription", true, Duration.ZERO, true);
    public CommandConfig editDescriptionCommand = new CommandConfig(Collections.singletonList("editdescription"), "Edit description", "/SMP editdescription <name>", "smp.editdescription", true, Duration.ofSeconds(3), true);
    public CommandConfig consoleCommand = new CommandConfig(Collections.singletonList("console"), "Send a console message to your server", "/SMP console <command>", "", true, Duration.ofSeconds(3), true);


    public CommandConfig deleteAllCommand = new CommandConfig(Collections.singletonList("deleteall"), "Delete all Servers", "/SMP deleteall <secret-key>", "smp.deleteall", true, Duration.ofSeconds(3), true);

}
