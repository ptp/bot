package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.settings.ConfigManager;

/**
 * Created by sean on 10/26/15.
 */
public class DiscordCommand extends Command {

    private ConfigManager configManager;

    public DiscordCommand(ConfigManager configManager) {
        super(SkypeUserRole.USER, "Get Discord group join link", CommandCategory.INFORMATIVE, "discord", "newchat");
        this.configManager = configManager;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        group.sendMessage(user.getUsername() + " - You can join our Discord chat by clicking " + Chat.link(configManager.getString("$.discord"), configManager.getString("$.discord")));
    }

}
