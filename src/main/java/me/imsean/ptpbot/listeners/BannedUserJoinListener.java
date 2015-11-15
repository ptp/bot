package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUserJoinEvent;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.mysql.UserManager;

public class BannedUserJoinListener implements SkypeEvent {

    private final UserManager userManager;

    public BannedUserJoinListener(UserManager userManager) {
        this.userManager = userManager;
    }

    public void onBannedUserJoin(SkypeConversationUserJoinEvent e) {
        if(!e.getConversation().isAdmin(PTPBot.getSkype().getSkypeUser(PTPBot.getSkype().getLocalUser().getUsername()))) return;
        if (e.getConversation().isAdmin(e.getUser())) {
            if (this.userManager.isBanned(e.getConversation(), e.getUser())) {
                e.getConversation().kick(e.getUser());
            }
        }
    }

}
