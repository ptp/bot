package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;

/**
 * Created by sean on 11/15/15.
 */
public class ReloadCommand extends Command {

    public ReloadCommand() {
        super(SkypeUserRole.ADMIN, "Reload the entire bot", CommandCategory.ADMINISTRATIVE, "reload");
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(PTPBot.getOwner().equals(user.getUsername())) {
            PTPBot bot = new PTPBot();
            bot.reload(group);
        }
    }

}
