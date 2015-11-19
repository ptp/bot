package me.imsean.ptpbot.api.mysql;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.exceptions.NotAdminException;
import me.imsean.ptpbot.exceptions.NotBannedFromGroupException;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final EzSkype skype;
    private final MySQLConnection connection;

    public UserManager(EzSkype skype, MySQLConnection connection) {
        this.skype = skype;
        this.connection = connection;
    }

    public void ban(SkypeConversation group, SkypeUser user) throws NotAdminException {
        if (group.isAdmin(PTPBot.getSkype().getSkypeUser(PTPBot.getSkype().getLocalUser().getUsername()))) {
            try {
                PreparedStatement stmt = this.connection.query("INSERT INTO `banned` (`username`, `group`) VALUES(?, ?)").getStatement();
                stmt.setString(1, user.getUsername());
                stmt.setString(2, group.getLongId());
                this.connection.getStatement().executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            group.kick(user);
        } else {
            throw new NotAdminException();
        }
    }

    public void unban(SkypeConversation group, SkypeUser user) throws NotBannedFromGroupException {
        try {
            if (isBanned(group, user)) {
                PreparedStatement stmt = this.connection.query("DELETE FROM `banned` WHERE `username`=? AND `group`=?").getStatement();
                stmt.setString(1, user.getUsername());
                stmt.setString(2, group.getLongId());
                this.connection.getStatement().executeUpdate();
                group.addUser(user);
            } else {
                throw new NotBannedFromGroupException();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
    }

    public boolean isBanned(SkypeConversation group, SkypeUser user) {
        try {
            PreparedStatement stmt = this.connection.query("SELECT * FROM `banned` WHERE `username`=?").getStatement();
            stmt.setString(1, user.getUsername());
            ResultSet result = this.connection.execute();
            while (result.next()) {
                if (user.getUsername().equals(result.getString("username"))
                        && group.getLongId().equalsIgnoreCase(result.getString("group"))) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
    }

    public boolean isBotAdmin(SkypeUser user) {
        try {
            PreparedStatement stmt = this.connection.query("SELECT * FROM `admins` WHERE `username`=?").getStatement();
            stmt.setString(1, user.getUsername());
            ResultSet result = this.connection.execute();
            while(result.next()) {
//                System.out.println(result.getString("username"));
                if (user.getUsername().equals(result.getString("username"))) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
    }

    public boolean isIgnored(String username) {
        try {
            PreparedStatement stmt = this.connection.query("SELECT * FROM `ignored` WHERE `username`=?").getStatement();
            stmt.setString(1, username);
            ResultSet result = this.connection.execute();
            while(result.next()) {
                if(username.equalsIgnoreCase(result.getString("username"))) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
    }

    public boolean isBotOwner(SkypeUser user) {
        return (PTPBot.getOwner().equals(user.getUsername()));
    }

    public void addBotAdmin(SkypeUser user) {
        try {
            PreparedStatement stmt = this.connection.query("INSERT INTO `admins` (`username`) VALUES(?)").getStatement();
            stmt.setString(1, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
    }

    public void ignoreUser(String username) {
        try {
            PreparedStatement stmt = this.connection.query("INSERT INTO `ignored` (`username`) VALUES(?)").getStatement();
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
    }

    public void removeBotAdmin(SkypeUser user) {
        try {
            PreparedStatement stmt = this.connection.query("DELETE FROM `admins` WHERE `username`=?").getStatement();
            stmt.setString(1, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
    }

    public void unIgnoreUser(String username) {
        try {
            PreparedStatement stmt = this.connection.query("DELETE FROM `ignored` WHERE `username`=?").getStatement();
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
    }

    public List<String> getAdminList() {
        List<String> admins = new ArrayList<String>();
        try {
            ResultSet results = this.connection.query("SELECT * FROM `admins`").execute();
            while(results.next()) {
                admins.add(results.getString("username"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
        return admins;
    }

    public List<String> getIgnoreList() {
        List<String> ignored = new ArrayList<String>();
        try {
            ResultSet results = this.connection.query("SELECT * FROM `ignored`").execute();
            while(results.next()) {
                ignored.add(results.getString("username"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
        return ignored;
    }

    public List<String> getBanList(SkypeConversation group) {
        List<String> banList = new ArrayList<String>();
        try {
            PreparedStatement stmt = this.connection.query("SELECT * FROM `banned` WHERE `group`=?").getStatement();
            stmt.setString(1, group.getLongId());
            ResultSet results = this.connection.execute();
            while(results.next()) {
                banList.add(results.getString("username"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query!", e);
        }
        return banList;
    }

    public boolean isGroupAdmin(SkypeConversation group, String username) {
        return group.isAdmin(PTPBot.getSkype().getSkypeUser(username));
    }

}
