package me.reb4ck.smp.api.command.commands.setlogo.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.setlogo.AbstractSetLogoCommand;
import me.reb4ck.smp.api.utils.BoxUtil;

public final class LobbySetLogoCommand extends AbstractSetLogoCommand {
    @Inject
    public LobbySetLogoCommand(BoxUtil boxUtil) {
        super(boxUtil);
    }
}
