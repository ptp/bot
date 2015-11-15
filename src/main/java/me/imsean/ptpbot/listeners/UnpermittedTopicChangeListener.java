package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUpdateTopicEvent;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.mysql.UserManager;
import me.imsean.ptpbot.exceptions.NotAdminException;

public class UnpermittedTopicChangeListener implements SkypeEvent {

    private final UserManager userManager;

    public UnpermittedTopicChangeListener(UserManager userManager) {
        this.userManager = userManager;
    }

    public void onTopicChange(SkypeConversationUpdateTopicEvent e) {
        if(!e.getConversation().isAdmin(PTPBot.getSkype().getSkypeUser(PTPBot.getSkype().getLocalUser().getUsername()))) return;
        if(PTPBot.getSkype().getLocalUser().getUsername().equalsIgnoreCase(e.getUser().getUsername()) || this.userManager.isBotAdmin(e.getUser())) return;
        try {
            this.userManager.ban(e.getConversation(), e.getUser());
            e.getConversation().changeTopic(e.getOldTopic());
        } catch (NotAdminException e1) {
            e1.printStackTrace();
        }
    }

}
