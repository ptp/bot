package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.PTPBot;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.mysql.StatsManager;
import me.imsean.ptpbot.api.mysql.UserManager;
import org.apache.commons.lang.time.DurationFormatUtils;

public class StatsCommand extends Command {

    private final UserManager userManager;
    private final StatsManager statsManager;

    private long start = System.currentTimeMillis();

    public static int recentMessageCount = 0;

    public StatsCommand(UserManager userManager, StatsManager statsManager) {
        super(SkypeUserRole.ADMIN, "Display various bot statistics", CommandCategory.INFORMATIVE, "stats", "statistics");
        this.userManager = userManager;
        this.statsManager = statsManager;
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if (this.userManager.isBotAdmin(user)) {
            long finish = System.currentTimeMillis();

            StringBuilder stats = new StringBuilder();
            stats.append("PTPBot Statistics - ");
            stats.append("\n").append(String.format("%,d", this.statsManager.getMessageCount())).append(" total messages received");
            stats.append("\n").append(String.format("%,d", recentMessageCount)).append(" messages received");
            stats.append("\n").append(PTPBot.getSkype().getConversations().size()).append(" groups");
            stats.append("\n").append(DurationFormatUtils.formatPeriod(start, finish, "'spent' d 'days,' H 'hours,' m 'mins and' s 'seconds online'"));
            group.sendMessage(stats.toString());

        }
    }

}
