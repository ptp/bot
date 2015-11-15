package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.mysql.UserManager;
import me.imsean.ptpbot.exceptions.NotBannedFromGroupException;

public class UnbanFromGroupCommand extends Command {

    private final UserManager userManager;

    public UnbanFromGroupCommand(UserManager userManager) {
        super(SkypeUserRole.ADMIN, "Unban user from current group", CommandCategory.ADMINISTRATIVE, "unban");
        this.userManager = userManager;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if (this.userManager.isBotAdmin(user)) {
            if(args.length == 0) {
                group.sendMessage(user.getUsername() + " - Usage: #unban (username)");
            }
            if(args.length > 0) {
                try {
                    this.userManager.unban(group, PTPBot.getSkype().getSkypeUser(args[0]));
                } catch (NotBannedFromGroupException e) {
                    group.sendMessage(user.getUsername() + " - This user is not banned from this group");
                }
            }
        }
    }

}
