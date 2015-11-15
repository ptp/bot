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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 10/26/15.
 */
public class ContactsCommand extends Command {

    private UserManager userManager;

    public ContactsCommand(UserManager userManager) {
        super(SkypeUserRole.ADMIN, "Manage contacts", CommandCategory.ADMINISTRATIVE, "contacts");
        this.userManager = userManager;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(this.userManager.isBotAdmin(user)) {
            if(args.length == 0) {
                group.sendMessage(user.getUsername() + " - Usage: #contacts (list/add/remove) (username)");
            }
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("list")) {
                    List<String> contactList = new ArrayList<String>();
                    String contacts = "";
                    contacts.concat("PTPBot Contact List - \n");

                    PTPBot.getSkype().getLocalUser().getContacts().forEach((consumer, contact) -> {
                        contactList.add((userManager.isBotAdmin(contact) ? Chat.bold("[bot_admin] ") : "") + contact.getUsername());
                    });
                    contacts.concat(StringUtils.join(contactList.iterator(), ", "));

                    group.sendMessage(contacts);
                }
                if(args[0].equalsIgnoreCase("add")) {
                    if(args[1].isEmpty()) {
                        group.sendMessage("Please specify a username!");
                        return;
                    }
                    SkypeUser contact = PTPBot.getSkype().getSkypeUser(args[1]);
                    if(contact.isContact()) {
                        group.sendMessage(args[1] + " is already a contact!");
                        return;
                    }
                    contact.setContact(true);
                    group.sendMessage(args[1] + " was added as a contact!");
                }
                if(args[0].equalsIgnoreCase("remove")) {
                    if(args[1].isEmpty()) {
                        group.sendMessage("Please specify a username!");
                        return;
                    }
                    SkypeUser contact = PTPBot.getSkype().getSkypeUser(args[1]);
                    if(!contact.isContact()) {
                        group.sendMessage(args[1] + " is not a contact!");
                        return;
                    }
                    contact.setContact(false);
                    group.sendMessage(args[1] + " was removed as a contact");
                }
            }
        }
    }

}
