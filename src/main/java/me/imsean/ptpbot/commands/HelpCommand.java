package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.command.CommandHandler;
import me.imsean.ptpbot.api.mysql.UserManager;

/**
 * Created by sean on 10/27/15.
 */
public class HelpCommand extends Command {

    private UserManager userManager;
    private CommandHandler commandHandler;

    public HelpCommand(UserManager userManager, CommandHandler commandHandler) {
        super(SkypeUserRole.ADMIN, "Brings up this help menu", CommandCategory.INFORMATIVE, "help");
        this.userManager = userManager;
        this.commandHandler = commandHandler;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        //if(!this.userManager.isBotAdmin(user)) return;
        Thread thread = new Thread(() -> {
            if(args.length == 0) {
                StringBuilder helpMenu = new StringBuilder();
                CommandCategory[] categories = CommandCategory.values();
                for(int i = 0; i < categories.length; i++) {
                    helpMenu.append("#help " + categories[i].name().toLowerCase() + "\n");
                }
                group.sendMessage(helpMenu.toString());
                return;
            }
            if(args.length > 0) {
                CommandCategory[] categories = CommandCategory.values();
                if(args[0].equalsIgnoreCase(CommandCategory.ADMINISTRATIVE.name())) {
                    StringBuilder commands = new StringBuilder();
                    this.commandHandler.getCommands().forEach((command) -> {
                        if(command.getCategory() == CommandCategory.ADMINISTRATIVE) {
                            if(this.userManager.isBotAdmin(user)) {
                                commands.append("#" + command.getNames()[0] + " - " + command.getUsage() + "\n");
                            } else if(command.getRole() == SkypeUserRole.USER) {
                                commands.append("#" + command.getNames()[0] + " - " + command.getUsage() + "\n");
                            }
                        }
                    });
                    if(commands.toString().isEmpty()) {
                        group.sendMessage("No commands in this category.");
                        return;
                    }
                    group.sendMessage(commands.toString());
                }
                if(args[0].equalsIgnoreCase(CommandCategory.INFORMATIVE.name())) {
                    StringBuilder commands = new StringBuilder();
                    this.commandHandler.getCommands().forEach((command) -> {
                        if(command.getCategory() == CommandCategory.INFORMATIVE) {
                            if(this.userManager.isBotAdmin(user)) {
                                commands.append("#" + command.getNames()[0] + " - " + command.getUsage() + "\n");
                            } else if(command.getRole() == SkypeUserRole.USER) {
                                commands.append("#" + command.getNames()[0] + " - " + command.getUsage() + "\n");
                            }
                        }
                    });
                    if(commands.toString().isEmpty()) {
                        group.sendMessage("No commands in this category.");
                        return;
                    }
                    group.sendMessage(commands.toString());
                }
                if (args[0].equalsIgnoreCase(CommandCategory.FUN.name())) {
                    StringBuilder commands = new StringBuilder();
                    this.commandHandler.getCommands().forEach((command) -> {
                        if(command.getCategory() == CommandCategory.FUN) {
                            if(this.userManager.isBotAdmin(user)) {
                                commands.append("#" + command.getNames()[0] + " - " + command.getUsage() + "\n");
                            } else if(command.getRole() == SkypeUserRole.USER) {
                                commands.append("#" + command.getNames()[0] + " - " + command.getUsage() + "\n");
                            }
                        }
                    });
                    if(commands.toString().isEmpty()) {
                        group.sendMessage("No commands in this category.");
                        return;
                    }
                    group.sendMessage(commands.toString());
                }
            }
//            StringBuilder commands = new StringBuilder();
//            commands.append("Available Commands - ");
//            for(Command command : this.commandHandler.getCommands()) {
//                if(this.userManager.isBotAdmin(user)) {
//                    commands.append("\n#" + command.getNames()[0] + " - " + command.getUsage());
//                } else {
//                    if(command.getRole() == SkypeUserRole.USER) {
//                        commands.append("\n#" + command.getNames()[0] + " - " + command.getUsage());
//                    }
//                }
//                //commands.append("\n #" + command.getNames()[0] + " - " + command.getUsage() + " - " + (command.getRole() == SkypeUserRole.ADMIN ? "admin only" : "public command"));
//            }
//            group.sendMessage(commands.toString());
        });
        thread.start();
    }

}
