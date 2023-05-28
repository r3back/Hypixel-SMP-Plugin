package me.reb4ck.smp.api.command.commands.manage.impl;

import com.google.inject.Inject;
import me.reb4ck.smp.api.command.commands.manage.AbstractManageCommand;
import me.reb4ck.smp.api.utils.BoxUtil;

public final class LobbyManageCommand extends AbstractManageCommand {
    @Inject
    public LobbyManageCommand(BoxUtil boxUtil){
        super(boxUtil);
    }
}
