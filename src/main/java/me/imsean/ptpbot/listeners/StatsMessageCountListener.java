package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.mysql.StatsManager;

public class StatsMessageCountListener implements SkypeEvent {

    private final StatsManager statsManager;

    public StatsMessageCountListener(StatsManager statsManager) {
        this.statsManager = statsManager;
    }

    public void onMessage(SkypeMessageReceivedEvent e) {
        if(e.getMessage().getSender().getUsername().equals(PTPBot.getSkype().getLocalUser().getUsername())) return;
        this.statsManager.addMessages();
    }

}
