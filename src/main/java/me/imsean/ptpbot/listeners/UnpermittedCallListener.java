package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationCallStartedEvent;

/**
 * Created by sean on 11/7/15.
 */
public class UnpermittedCallListener implements SkypeEvent {

    public void onCall(SkypeConversationCallStartedEvent e) {
        SkypeConversation group = e.getConversation();
        group.sendMessage(e.getUser().getUsername() + " started a call");
    }

}
