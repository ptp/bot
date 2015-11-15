package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.mysql.UserManager;

public class AdminsCommand extends Command {

    private final UserManager userManager;
    private final EzSkype skype;

    public AdminsCommand(EzSkype skype, UserManager userManager) {
        super(SkypeUserRole.ADMIN, "Management command for Admins", CommandCategory.ADMINISTRATIVE, "admins");
        this.userManager = userManager;
        this.skype = skype;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if (this.userManager.isBotOwner(user)) {
            if(args.length == 0) {
                group.sendMessage(user.getUsername() + " - Usage: #admins (add/remove/list) (username)");
            }
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("add")) {
                    this.userManager.addBotAdmin(PTPBot.getSkype().getSkypeUser(args[1].trim()));
                    group.sendMessage(user.getUsername() + " - " + String.valueOf(args[1]) + " is now a BOT_ADMIN");
                }
                if(args[0].equalsIgnoreCase("remove")) {
                    this.userManager.removeBotAdmin(PTPBot.getSkype().getSkypeUser(args[1].trim()));
                    group.sendMessage(user.getUsername() + " - " + String.valueOf(args[1]) + " is now a BOT_USER");
                }
                if(args[0].equalsIgnoreCase("list")) {
                    Thread thread = new Thread(() -> {
                        StringBuilder admins = new StringBuilder();
                        admins.append("PTPBot Admins - ");
                        for(String admin : this.userManager.getAdminList()) {
                            admins.append("\n" + admin);
                        }
                        group.sendMessage(admins.toString());
                    });
                    thread.start();
                }
            }
        }
    }
}
