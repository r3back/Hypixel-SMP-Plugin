package me.reb4ck.smp.api.command.commands.console.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.console.AbstractConsoleCommand;
import me.reb4ck.smp.api.utils.BoxUtil;

public final class LobbyConsoleCommand extends AbstractConsoleCommand {
    @Inject
    public LobbyConsoleCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }
}
