package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.mysql.UserManager;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 10/31/15.
 */
public class GroupManageCommand extends Command {

    private UserManager userManager;
    private String usage = "Usage: group (kick/kickall/setadmin/add/listusers) (user) (boolean)";

    public GroupManageCommand(UserManager userManager) {
        super(SkypeUserRole.ADMIN, "Manage group", CommandCategory.INFORMATIVE, "group");
        this.userManager = userManager;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(!this.userManager.isBotAdmin(user)) return;
        if(args.length == 0) {
            group.sendMessage(user.getUsername() + " - " + this.usage);
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("kick")) {
                if(args[1].isEmpty()) {
                    group.sendMessage(user.getUsername() + " - " + this.usage);
                    return;
                }
                if(this.userManager.isBotAdmin(PTPBot.getSkype().getSkypeUser(args[1]))) {
                    group.sendMessage(user.getUsername() + " - are u srs???");
                }
                if(!this.userManager.isGroupAdmin(group, PTPBot.getSkype().getLocalUser().getUsername())) {
                    group.sendMessage(user.getUsername() + " - I am not ADMIN in this group");
                    return;
                }
                group.kick(PTPBot.getSkype().getSkypeUser(args[1].trim()));
            }
            if(args[0].equalsIgnoreCase("kickall")) {
                if(!this.userManager.isGroupAdmin(group, PTPBot.getSkype().getLocalUser().getUsername())) {
                    group.sendMessage(user.getUsername() + " - I am not ADMIN in this group");
                    return;
                }
                group.getUsers().forEach((chatter) -> {
                    if(this.userManager.isBotAdmin(chatter)) return;
                    if(PTPBot.getSkype().getLocalUser().getUsername().equalsIgnoreCase(chatter.getUsername())) return;
                    group.kick(chatter);
                });
            }
            if(args[0].equalsIgnoreCase("setadmin")) {
                if(args[1].isEmpty()) {
                    group.sendMessage(user.getUsername() + " - " + this.usage);
                    return;
                }
                if(!this.userManager.isGroupAdmin(group, PTPBot.getSkype().getLocalUser().getUsername())) {
                    group.sendMessage(user.getUsername() + " - I am not ADMIN in this group");
                    return;
                }
                if(args[2].equalsIgnoreCase("true")) {
                    group.setUserRole(PTPBot.getSkype().getSkypeUser(args[1]), SkypeUserRole.ADMIN);
                }
                if(args[2].equalsIgnoreCase("false")) {
                    group.setUserRole(PTPBot.getSkype().getSkypeUser(args[1]), SkypeUserRole.USER);
                }
            }
            if(args[0].equalsIgnoreCase("add")) {
                if(args[1].isEmpty()) {
                    group.sendMessage(user.getUsername() + " - " + this.usage);
                    return;
                }
                group.addUser(PTPBot.getSkype().getSkypeUser(args[1].trim()));
            }
            if(args[0].equalsIgnoreCase("listusers")) {

                SkypeMessage userList = group.sendMessage("Loading...");

                UserManager userManager = this.userManager;

                Thread thread = new Thread(() -> {
                    List<String> participants = new ArrayList<String>();

                    StringBuilder users = new StringBuilder();
                    users.append(Chat.bold(StringUtils.abbreviate("[" + group.getTopic(), 25) + "] Participants [" + group.getUsers().size() + "]") + "\n");

                    group.getUsers().forEach((participant) -> {
                        participants.add((userManager.isBotAdmin(participant) ? Chat.bold("[bot_admin] ") : "") + (userManager.isGroupAdmin(group, participant.getUsername()) ? Chat.bold("[group_admin] ") : "") + participant.getUsername());
                    });

                    users.append(StringUtils.join(participants.iterator(), ", "));

                    userList.edit(users.toString());
                });
                thread.start();
            }
        }
    }

}
