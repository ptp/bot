package me.imsean.ptpbot;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.SkypeStatus;
import in.kyle.ezskypeezlife.events.EventManager;
import me.imsean.ptpbot.api.mysql.MySQLConnection;
import me.imsean.ptpbot.api.mysql.StatsManager;
import me.imsean.ptpbot.api.mysql.UserManager;
import me.imsean.ptpbot.api.settings.ConfigManager;
import me.imsean.ptpbot.listeners.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sean on 10/25/15.
 */
public class PTPBot {

    private static final String owner = "master_zombiecow";
    private static EzSkype skype;
    private static MySQLConnection connection;
    private static UserManager userManager;
    private static StatsManager statsManager;
    private static ConfigManager configManager;

    private boolean running;

    public static String getOwner() {
        return owner;
    }

    public static EzSkype getSkype() {
        return skype;
    }

    public static MySQLConnection getConnection() {
        return connection;
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static ConfigManager getConfigManager() { return configManager; }

    public void login(String username, String password) {
        if(this.running) {
            stop();
        }

        try {
            skype = new EzSkype(new SkypeCredentials(username, password));
            skype.login();
            skype.getLocalUser().fullyLoad();
            skype.setStatus(SkypeStatus.ONLINE);

            Timer timer = new Timer();
            TimerTask threeHrTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        EzSkype newSkype = new EzSkype(new SkypeCredentials(username, password)).login();
                        while(!newSkype.getLocalUser().isLoaded());
                        skype.logout();
                        skype = newSkype;
                        newSkype = null;
                        skype.getLocalUser().fullyLoad();
                        skype.setStatus(SkypeStatus.ONLINE);
                        System.out.println("New skype instance made!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.schedule(threeHrTask, 1000 * 60 * 360, 1000 * 60 * 360);

            System.out.println("Successfully logged in!");
        } catch (Exception e) {
            throw new RuntimeException("An exception occurred whilst logging in!", e);
        }
    }

    public void start() {
        this.running = true;

        configManager = new ConfigManager();
        configManager.loadConfig("config.json");
        connection = new MySQLConnection(configManager.getString("$.mysql.host"), configManager.getString("$.mysql.database"),
                configManager.getString("$.mysql.username"), configManager.getString("$.mysql.password"));
        userManager = new UserManager(skype, connection);
        statsManager = new StatsManager();
        registerListeners();

        System.out.println("PTPBot has started!");
    }

    public void reload() {
        /* Ignore this PLS */
        this.running = false;

        configManager = null;
        connection = null;
        configManager = null;
        userManager = null;
        statsManager = null;

        configManager = new ConfigManager();
        configManager.loadConfig("config.json");
        connection = new MySQLConnection(configManager.getString("$.mysql.host"), configManager.getString("$.mysql.database"),
                configManager.getString("$.mysql.username"), configManager.getString("$.mysql.password"));
        userManager = new UserManager(skype, connection);
        statsManager = new StatsManager();

        registerListeners();

        this.running = true;
    }

    public void stop() {
        this.running = false;
        skype.logout();
        skype = null;
        configManager = null;
        connection = null;
        userManager = null;
        statsManager = null;
    }

    public void registerListeners() {
        if(skype == null) {
            return;
        }
        final EventManager em = skype.getEventManager();

        em.registerEvents(new ContactRequestListener());
        em.registerEvents(new CommandListener(userManager, configManager, statsManager));
        em.registerEvents(new BannedUserJoinListener(userManager));
        em.registerEvents(new StatsMessageCountListener(statsManager));
        em.registerEvents(new UnpermittedTopicChangeListener(userManager));
        em.registerEvents(new UnpermittedTopicPictureChangeListener(userManager));
        em.registerEvents(new GuestJoinListener(userManager));
        em.registerEvents(new GuestChatListener());
        em.registerEvents(new BannedUserChatListener(userManager));
        em.registerEvents(new ChatColorListener());
    }

}
