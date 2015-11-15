package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.user.SkypeContactRequestEvent;

public class ContactRequestListener implements SkypeEvent {

    public void onContactRequest(SkypeContactRequestEvent e) {
        System.out.println("New Contact: " + e.getSkypeUser().getUsername());
        e.getSkypeUser().setContact(true);
    }
}
