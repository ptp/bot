package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationPictureChangeEvent;
import me.imsean.ptpbot.api.mysql.UserManager;
import me.imsean.ptpbot.exceptions.NotAdminException;

/**
 * Created by sean on 11/28/15.
 */
public class PingListener implements SkypeEvent {

    private UserManager userManager;

    public PingListener(UserManager userManager) {
        this.userManager = userManager;
    }

    public void onPing(SkypeConversationPictureChangeEvent e) {
        if(!this.userManager.isBotAdmin(e.getUser())) {
            try {
                this.userManager.ban(e.getConversation(), e.getUser());
            } catch (NotAdminException e1) {
                e1.printStackTrace();
            }
        }
    }

}
