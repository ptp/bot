package me.imsean.ptpbot.api.command;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;

/**
 * Created by sean on 10/25/15.
 */
public abstract class Command {

    private final SkypeUserRole role;
    private final CommandCategory category;
    private final String usage;
    private final String[] names;

    public Command(SkypeUserRole role, String usage, CommandCategory category, String... names) {
        this.role = role;
        this.usage = usage;
        this.category = category;
        this.names = names;
    }

    public SkypeUserRole getRole() {
        return role;
    }

    public CommandCategory getCategory() { return category; }

    public String getUsage() { return usage; }

    public String[] getNames() {
        return names;
    }

    public abstract void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args);
}
