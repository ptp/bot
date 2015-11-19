package me.imsean.ptpbot.api.command;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.mysql.StatsManager;
import me.imsean.ptpbot.api.mysql.UserManager;
import me.imsean.ptpbot.api.settings.ConfigManager;
import me.imsean.ptpbot.commands.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHandler {

    public static final String prefix = ".";

    private UserManager userManager;
    private StatsManager statsManager;
    private ConfigManager configManager;
    private List<Command> commands;

    public CommandHandler(UserManager userManager, ConfigManager configManager, StatsManager statsManager) {
        this.userManager = userManager;
        this.configManager = configManager;
        this.statsManager = statsManager;
        this.commands = new ArrayList<>();

        addCommands(
                new BanFromGroupCommand(this.userManager),
                new UnbanFromGroupCommand(this.userManager),
                new JoinCommand(),
                new StatsCommand(this.userManager, this.statsManager),
                new AdminsCommand(PTPBot.getSkype(), this.userManager),
                new BanListCommand(this.userManager),
                new ContactsCommand(this.userManager),
                new DiscordCommand(this.configManager),
                new GuestCommand(this.userManager),
                new GroupManageCommand(this.userManager),
                new DancePartyCommand(this.userManager),
                new HelpCommand(this.userManager, this),
                new ColorCommand(),
                new IgnoreCommand(this.userManager),
                new ImageCommand()
        );
    }

    public List<Command> getCommands() {
        return commands;
    }

    private void addCommands(Command... commands) {
        Collections.addAll(this.commands, commands);
    }


    public void handleCommand(SkypeConversation group, SkypeUser user, SkypeMessage message) {
        String input = message.getMessage();

        if (!input.startsWith(prefix)) return;

        String noPrefix = input.substring(prefix.length());
        String[] args = noPrefix.split(" ");

        for (Command c : commands) {
            for (String name : c.getNames()) {
                if (name.equalsIgnoreCase(args[0])) {
                    List<String> newArgs = new ArrayList<String>();
                    for (String s : args) if (!args[0].equalsIgnoreCase(s)) newArgs.add(s);
                    String[] arrayArgs = newArgs.toArray(new String[newArgs.size()]);

                    c.onCommand(message, group, user, arrayArgs);
                    break;
                }
            }
        }
    }

}