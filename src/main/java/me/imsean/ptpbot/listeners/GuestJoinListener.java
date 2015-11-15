package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUserJoinEvent;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.mysql.UserManager;

/**
 * Created by sean on 10/20/15.
 */
public class GuestJoinListener implements SkypeEvent {

    private final UserManager userManager;

    public GuestJoinListener(UserManager userManager) {
        this.userManager = userManager;
    }

    public void onGuestJoinListener(SkypeConversationUserJoinEvent e) {
        if(!e.getConversation().isAdmin(PTPBot.getSkype().getSkypeUser(PTPBot.getSkype().getLocalUser().getUsername()))) return;
        String username = e.getUser().getUsername();
        if(username.startsWith("guest:")) {
            e.getConversation().kick(e.getUser());
        }
    }

}
