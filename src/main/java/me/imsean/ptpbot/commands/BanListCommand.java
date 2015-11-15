package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.mysql.UserManager;

/**
 * Created by sean on 10/20/15.
 */
public class BanListCommand extends Command {

    private final UserManager userManager;

    public BanListCommand(UserManager userManager) {
        super(SkypeUserRole.ADMIN, "List banned users in group", CommandCategory.ADMINISTRATIVE, "banlist");
        this.userManager = userManager;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(this.userManager.isBotAdmin(user)) {
            StringBuilder banned = new StringBuilder();
            banned.append("Banned users in this group - ");
            for(String ban : this.userManager.getBanList(group)) {
                banned.append("\n" + ban);
            }
            group.sendMessage(banned.toString());
        }
    }
}
