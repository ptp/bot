package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.mysql.UserManager;

/**
 * Created by sean on 11/15/15.
 */
public class IgnoreCommand extends Command {

    private UserManager userManager;

    public IgnoreCommand(UserManager userManager) {
        super(SkypeUserRole.ADMIN, "Disallow certain users to interact with PTPBot", CommandCategory.ADMINISTRATIVE, "ignore");
        this.userManager = userManager;
    }

    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(args.length == 0) {
            group.sendMessage(user.getUsername() + " - Usage: #ignore (username) (boolean)");
        }
        if(args.length == 2) {
            if(args[0].isEmpty() || args[1].isEmpty()) {
                group.sendMessage(user.getUsername() + " - Usage: #ignore (username) (boolean)");
                return;
            }
            String username = args[0];
            boolean ignored = Boolean.parseBoolean(args[1]);
            if(ignored) {
                if(username.equalsIgnoreCase(PTPBot.getOwner())) {
                    group.sendMessage(user.getUsername() + " - are u srs???");
                    return;
                }
                this.userManager.ignoreUser(username);
                group.sendMessage(user.getUsername() + " - " + username + " is now being ignored");
            } else {
                if(!this.userManager.isIgnored(username)) {
                    group.sendMessage(user.getUsername() + " - " + username + " is not being ignored");
                    return;
                }
                this.userManager.unIgnoreUser(username);
                group.sendMessage(user.getUsername() + " - " + username + " is no longer being ignored");
            }
        } else {
            group.sendMessage(user.getUsername() + " - Usage: #ignore (username) (boolean)");
        }
    }

}
