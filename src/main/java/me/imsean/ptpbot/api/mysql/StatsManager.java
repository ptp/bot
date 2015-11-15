package me.imsean.ptpbot.api.mysql;

import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandHandler;
import me.imsean.ptpbot.api.settings.ConfigManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StatsManager {

    private final UserManager userManager;
    private final ConfigManager configManager;
    private final Connection connection;

    public StatsManager() {
        this.userManager = PTPBot.getUserManager();
        this.configManager = PTPBot.getConfigManager();
        this.connection = PTPBot.getConnection();
    }

    public int getMessageCount() {
        int messages = 0;
        try {
            PreparedStatement stmt = this.connection.query("SELECT `counter` FROM `messages`").getStatement();
            ResultSet result = this.connection.execute();
            while(result.next()) {
                messages = result.getInt("counter");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public int getCommandCount() {
        CommandHandler ch = new CommandHandler(this.userManager, this.configManager, this);
        List<Command> commands = ch.getCommands();
        return commands.size();
    }

    public void addMessages() {
        try {
            PreparedStatement stmt = this.connection.query("UPDATE `messages` SET `counter` = `counter` + 1").getStatement();
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
