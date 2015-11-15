package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.CommandHandler;
import me.imsean.ptpbot.api.mysql.StatsManager;
import me.imsean.ptpbot.api.mysql.UserManager;
import me.imsean.ptpbot.api.settings.ConfigManager;
import org.apache.commons.lang3.StringUtils;

public class CommandListener implements SkypeEvent {

    private final CommandHandler commandHandler;
    private final UserManager userManager;
    private final ConfigManager configManager;

    public CommandListener(UserManager userManager, ConfigManager configManager, StatsManager statsManager) {
        this.userManager = userManager;
        this.configManager = configManager;
        this.commandHandler = new CommandHandler(userManager, configManager, statsManager);
    }

    public void onCommand(SkypeMessageReceivedEvent e) {
        if(!e.getMessage().isEdited()) {
            if(e.getMessage().getMessage().startsWith(CommandHandler.prefix)) {
                if(PTPBot.getSkype().getLocalUser().getUsername().equalsIgnoreCase(e.getMessage().getSender().getUsername())) return;
                if(this.userManager.isIgnored(e.getMessage().getSender().getUsername())) return;
                this.commandHandler.handleCommand(e.getMessage().getConversation(), e.getMessage().getSender(), e.getMessage());
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("[" + StringUtils.abbreviate(e.getMessage().getConversation().getTopic(), 15) + "] ");
        builder.append(e.getMessage().getSender().getDisplayName() + " (" + e.getMessage().getSender().getUsername() + ") : ");
        builder.append(e.getMessage().getMessage());
        System.out.println(builder.toString());
    }

}
