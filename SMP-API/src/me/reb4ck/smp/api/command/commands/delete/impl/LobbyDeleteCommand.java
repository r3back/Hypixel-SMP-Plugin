package me.reb4ck.smp.api.command.commands.delete.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.delete.AbstractDeleteCommand;
import me.reb4ck.smp.api.utils.BoxUtil;

public final class LobbyDeleteCommand extends AbstractDeleteCommand {
    @Inject
    public LobbyDeleteCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }
}
