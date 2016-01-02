package me.imsean.ptpbot;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.SkypeStatus;
import in.kyle.ezskypeezlife.api.captcha.SkypeCaptcha;
import in.kyle.ezskypeezlife.api.captcha.SkypeErrorHandler;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.events.EventManager;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import me.imsean.ptpbot.api.mysql.MySQLConnection;
import me.imsean.ptpbot.api.mysql.StatsManager;
import me.imsean.ptpbot.api.mysql.UserManager;
import me.imsean.ptpbot.api.settings.ConfigManager;
import me.imsean.ptpbot.listeners.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by sean on 10/25/15.
 */
public class PTPBot implements SkypeErrorHandler {

    private static final String owner = "master_zombiecow";
    private static EzSkype skype;
    private static MySQLConnection connection;
    private static UserManager userManager;
    private static StatsManager statsManager;
    private static ConfigManager configManager;
    private static List<SkypeEvent> listeners = new ArrayList<>();

    private boolean running;

    public PTPBot() {
    }

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
            skype.setErrorHandler(this);
            skype.login();
            skype.setStatus(SkypeStatus.ONLINE);

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
        setupListeners();
        registerListeners();

        System.out.println("PTPBot has started!");
    }

    public void stop() {
        this.running = false;
        unregisterListeners();
        skype.logout();
        skype = null;
        configManager = null;
        connection = null;
        userManager = null;
        statsManager = null;
    }

    public void reload(SkypeConversation group) {
        this.running = false;

        ConfigManager config = new ConfigManager();
        config.loadConfig("config.json");

        EzSkype newSkype = new EzSkype(config.getString("$.skype.username"), config.getString("$.skype.password"));
        skype.logout();
        skype = newSkype;
        try {
            skype.login();
            final EventManager em = skype.getEventManager();
            listeners.forEach(em::unregisterEvents);
            listeners.forEach(em::registerEvents);
            System.out.println("PTPBot reloaded!");
            group.sendMessage("PTPBot reloaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupListeners() {
        if(skype == null) {
            return;
        }
        listeners.add(new ContactRequestListener());
        listeners.add(new CommandListener(userManager, configManager, statsManager));
        listeners.add(new BannedUserJoinListener(userManager));
        listeners.add(new StatsMessageCountListener(statsManager));
        listeners.add(new UnpermittedTopicChangeListener(userManager));
        listeners.add(new UnpermittedTopicPictureChangeListener(userManager));
        listeners.add(new GuestJoinListener(userManager));
        listeners.add(new GuestChatListener());
        listeners.add(new BannedUserChatListener(userManager));
        listeners.add(new ChatColorListener());
        listeners.add(new PingListener(userManager));
    }

    public void registerListeners() {
        if(skype == null) {
            return;
        }
        final EventManager em = skype.getEventManager();
        listeners.forEach(em::registerEvents);
    }

    public void unregisterListeners() {
        if(skype == null) {
            return;
        }
        final EventManager em = skype.getEventManager();
        listeners.forEach(em::unregisterEvents);
    }

    @Override
    public String solve(SkypeCaptcha skypeCaptcha) {
        System.out.println("Enter the solution to " + skypeCaptcha.getUrl() + " then click enter");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public String setNewPassword() {
        System.out.println("Set new password!");
        return null;
    }

}
