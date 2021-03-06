package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;

public class JoinCommand extends Command {

    public JoinCommand() {
        super(SkypeUserRole.ADMIN, "Disabled for now", CommandCategory.ADMINISTRATIVE, "join");
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        group.sendMessage("This command is disabled at the moment!");
    }

}
