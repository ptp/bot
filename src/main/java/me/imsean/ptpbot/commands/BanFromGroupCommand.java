package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.mysql.UserManager;
import me.imsean.ptpbot.exceptions.NotAdminException;

public class BanFromGroupCommand extends Command {

    private final UserManager userManager;

    public BanFromGroupCommand(UserManager userManager) {
        super(SkypeUserRole.ADMIN, "Ban users from current group", CommandCategory.ADMINISTRATIVE, "ban");
        this.userManager = userManager;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if (!this.userManager.isBotAdmin(user)) return;

        if(args.length == 0) {
            group.sendMessage(user.getUsername() + " - Usage: ban (username)");
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase(PTPBot.getOwner()) || args[0].equalsIgnoreCase(PTPBot.getSkype().getLocalUser().getUsername())) {
                group.sendMessage(user.getUsername() + " - r u srs???");
                return;
            }
            try {
                for(SkypeUser gu : group.getUsers()) {
                    if(gu.getUsername().equals(args[0])) {
                        if (this.userManager.isBotAdmin(PTPBot.getSkype().getSkypeUser(args[0]))) {
                            group.sendMessage(user.getUsername() + " - r u srs????");
                            return;
                        }
                        this.userManager.ban(group, PTPBot.getSkype().getSkypeUser(args[0]));
                    }
                }
            } catch (NotAdminException e) {
                group.sendMessage(user.getUsername() + " - I am not ADMIN in this group");
            }
        }
    }

}
