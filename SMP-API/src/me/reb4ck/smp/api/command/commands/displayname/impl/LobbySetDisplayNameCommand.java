package me.reb4ck.smp.api.command.commands.displayname.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.displayname.AbstractDisplayNameCommand;
import me.reb4ck.smp.api.utils.BoxUtil;

public final class LobbySetDisplayNameCommand extends AbstractDisplayNameCommand {
    @Inject
    public LobbySetDisplayNameCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }
}
