package me.imsean.ptpbot;

import me.imsean.ptpbot.api.settings.ConfigManager;

/**
 * Created by sean on 10/25/15.
 */
public class Main {

    public static PTPBot bot;

    public static void main(String[] args)  {
        if(args.length > 0) {
            System.getProperty("http.proxyHost", args[0]);
            System.getProperty("http.proxyPort", args[1]);
            System.out.println("Now using HTTP proxy: " + args[0] + ":" + args[1]);
        }
        ConfigManager config = new ConfigManager();
        config.loadConfig("config.json");

        bot = new PTPBot();
        bot.login(config.getString("$.skype.username"), config.getString("$.skype.password"));
        bot.start();
    }

}
