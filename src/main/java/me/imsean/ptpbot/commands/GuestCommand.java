package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.mysql.UserManager;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 10/26/15.
 */
public class GuestCommand extends Command {

    private UserManager userManager;

    public GuestCommand(UserManager userManager) {
        super(SkypeUserRole.ADMIN, "Management command for Guests", CommandCategory.ADMINISTRATIVE, "guests");
        this.userManager = userManager;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(!this.userManager.isBotAdmin(user)) return;
        if(args.length == 0) {
            group.sendMessage(user.getUsername() + " - Usage: guests (remove/list)");
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("list")) {

                SkypeMessage guestList = group.sendMessage("Loading...");

                UserManager userManager = this.userManager;

                Thread thread = new Thread(() -> {
                    List<String> guests = new ArrayList<String>();
                    StringBuilder glist = new StringBuilder();

                    group.getUsers().forEach((guest) -> {
                       if(guest.getUsername().startsWith(":guest")) {
                           guests.add(guest.getUsername());
                       }
                    });

                    glist.append(StringUtils.join(guests.iterator(), ", "));

                    guestList.edit(glist.toString());
                });
                thread.start();
            }
            if(args[0].equalsIgnoreCase("remove")) {
                Thread thread = new Thread(() -> {
                    if(!group.isAdmin(PTPBot.getSkype().getSkypeUser(PTPBot.getSkype().getLocalUser().getUsername()))) return;
                    int amountRemoved = 0;
                    for(SkypeUser guest : group.getUsers()) {
                        if(guest.getUsername().startsWith("guest:")) {
                            amountRemoved++;
                            group.kick(guest);
                        }
                    }
                    group.sendMessage(user.getUsername() + " - Removed " + amountRemoved + " guests");
                });
                thread.start();
            }
        }
    }

}
