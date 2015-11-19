package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.util.HexValidator;

import java.util.Arrays;

/**
 * Created by sean on 11/3/15.
 */
public class ColorCommand extends Command {

    private final HexValidator hexer = new HexValidator();

    public ColorCommand() {
        super(SkypeUserRole.USER, "Understand color better", CommandCategory.INFORMATIVE, "color");
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(args.length == 0) {
            group.sendMessage(user.getUsername() + " - Usage: color (image/literal) (color) (text)");
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("image")) {
                group.sendMessage("This feature is currently being worked on");
            }
            if(args[0].equalsIgnoreCase("literal")) {
                if(args.length == 2) {
                    Thread thread = new Thread(() -> {
                        String color = args[1].trim();
                        if(!hexer.validate(color)) {
                            group.sendMessage("Incorrect format, correct format ex. #ababab");
                            return;
                        }
                        group.sendMessage(Chat.color("█████████████", color) + "\n" + Chat.color("█████████████", color) + "\n" + Chat.color("█████████████", color));
                    });
                    thread.start();
                    return;
                }
                if(args.length >= 3) {
                    Thread thread = new Thread(() -> {
                        String color = args[1].trim();
                        if(!hexer.validate(color)) {
                            group.sendMessage("Incorrect format, correct format ex. #ababab");
                            return;
                        }
                        String[] textarr = Arrays.copyOfRange(args, 2, args.length);
                        StringBuilder text = new StringBuilder();
                        for(int i = 0; i < textarr.length; i++) {
                            text.append(textarr[i]).append(" ");
                        }
                        group.sendMessage(Chat.color(text.toString(), color));
                    });
                    thread.start();
                }
            }
        }
    }

}
