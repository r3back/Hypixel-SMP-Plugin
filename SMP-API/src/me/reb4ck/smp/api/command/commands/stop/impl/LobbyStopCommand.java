package me.reb4ck.smp.api.command.commands.stop.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.stop.AbstractStopCommand;
import me.reb4ck.smp.api.utils.BoxUtil;

public final class LobbyStopCommand extends AbstractStopCommand {
    @Inject
    public LobbyStopCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }
}