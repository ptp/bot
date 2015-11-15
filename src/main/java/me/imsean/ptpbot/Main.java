package me.imsean.ptpbot;

import me.imsean.ptpbot.api.settings.ConfigManager;

/**
 * Created by sean on 10/25/15.
 */
public class Main {

    public static void main(String[] args)  {
        ConfigManager config = new ConfigManager();
        config.loadConfig("config.json");

        PTPBot bot = new PTPBot();
        bot.login(config.getString("$.skype.username"), config.getString("$.skype.password"));
        bot.start();
    }

}
