package me.imsean.ptpbot.listeners;

import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import me.imsean.ptpbot.api.util.HexValidator;

import java.util.Arrays;

/**
 * Created by sean on 11/3/15.
 */
public class ChatColorListener implements SkypeEvent {

    private final HexValidator hexer = new HexValidator();

    public void onChatEvent(SkypeMessageReceivedEvent e) {
        if(e.getMessage().isEdited()) return;
        String[] args = e.getMessage().getMessage().split(" ");
        if(args[0].startsWith("#")) {
            String color = args[0];
            SkypeConversation group = e.getMessage().getConversation();
            if(args.length < 2) {
                if(hexer.validate(args[0].trim())) {
                    group.sendMessage(Chat.color("█████████████", color) + "\n" + Chat.color("█████████████", color) + "\n" + Chat.color("█████████████", color));
                }
                return;
            }
            if(args.length > 0) {
                if(hexer.validate(args[0].trim())) {
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    StringBuilder textArr = new StringBuilder();
                    for(int i = 0; i < newArgs.length; i++) {
                        textArr.append(newArgs[i].trim()).append(" ");
                    }
                    group.sendMessage(Chat.color(textArr.toString(), color));
                }
                return;
            }
        }
    }

}
