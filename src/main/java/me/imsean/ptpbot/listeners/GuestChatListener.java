package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import me.imsean.ptpbot.PTPBot;

/**
 * Created by sean on 10/26/15.
 */
public class GuestChatListener implements SkypeEvent {

    public void onGuestChatListener(SkypeMessageReceivedEvent e) {
        if(!e.getMessage().getConversation().isAdmin(PTPBot.getSkype().getSkypeUser(PTPBot.getSkype().getLocalUser().getUsername()))) return;
        if(e.getMessage().getSender().getUsername().startsWith(":guest")) {
            e.getMessage().getConversation().kick(e.getMessage().getSender());
        }
    }

}
