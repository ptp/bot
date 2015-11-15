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
        if(true) {
            group.sendMessage("This command is disabled at the moment!");
            return;
        }
        if(args.length == 0) {
            group.sendMessage(user.getUsername() + " - Usage: #join (link)");
        }
        if(args.length > 0) {
            System.out.println(message.getMessage().split(" ")[0]);
//            if(UrlValidator.getInstance().isValid(href)) {
//                PTPBot.getSkype().joinInviteLink(href);
//            } else {
//                group.sendMessage(user.getUsername() + " - Invalid Join Link");
//            }
        }
    }

}
