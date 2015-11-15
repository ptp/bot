package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.mysql.UserManager;

/**
 * Created by sean on 10/26/15.
 */
public class BannedUserChatListener implements SkypeEvent {

    private UserManager userManager;

    public BannedUserChatListener(UserManager userManager) {
        this.userManager = userManager;
    }

    public void onBannedUserChatListener(SkypeMessageReceivedEvent e) {
        if(!e.getMessage().getConversation().isAdmin(PTPBot.getSkype().getSkypeUser(PTPBot.getSkype().getLocalUser().getUsername()))) return;
        if(this.userManager.isBanned(e.getMessage().getConversation(), e.getMessage().getSender())) {
            e.getMessage().getConversation().kick(e.getMessage().getSender());
        }
    }

}
