package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.mysql.UserManager;

/**
 * Created by sean on 11/1/15.
 */
public class DancePartyCommand extends Command {

    private UserManager userManager;

    public DancePartyCommand(UserManager userManager) {
        super(SkypeUserRole.ADMIN, "sik beats bro", CommandCategory.FUN, "danceparty", "dp");
        this.userManager = userManager;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(this.userManager.isBotAdmin(user)) {

            if(args.length == 0) {
                group.sendMessage(user.getUsername() + " - Usage: dp (message)");
                return;
            }

            Thread thread = new Thread(() -> {
                SkypeMessage amazing = group.sendMessage("Loading da sik beats bro...");
                StringBuilder amazingWords = new StringBuilder();
                if(args.length > 0) {
                    for (String arg : args) {
                        amazingWords.append(Chat.color(arg.trim(), hexCodeGenerator())).append(" ");
                    }
                    amazing.edit(amazingWords.toString());
                }
            });
            thread.start();

//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Random rn = new Random();
//                    int range = 20 + rn.nextInt(200 - 20 + 1);
//                    for(int i = 0; i < range; i++) {
//                        amazingWords.append(Chat.color(getRandomWord(25), hexCodeGenerator()));
//                    }            try {
//                        TimeUnit.SECONDS.sleep(5);
//                        amazing.edit(amazingWords.toString());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.start();
        }
    }

    public String getRandomWord(int length) {
        String r = "";
        for(int i = 0; i < length; i++) {
            r += (char)(Math.random() * 26 + 97);
        }
        return r;
    }

    public String hexCodeGenerator() {
        String[] letters = new String[15];
        letters = "0123456789ABCDEF".split("");
        String code ="#";
        for(int i = 0; i < 6; i++) {
            double ind = Math.random() * 15;
            int index = (int)Math.round(ind);
            code += letters[index];
        }
        return code;
    }

}
