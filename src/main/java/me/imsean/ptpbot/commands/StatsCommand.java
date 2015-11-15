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
            stats.append("\n Received " + this.statsManager.getMessageCount() + " messages");
            stats.append("\n Added " + PTPBot.getSkype().getLocalUser().getContacts().size() + " contacts");
            stats.append("\n Implemented " + this.statsManager.getCommandCount() + " commands");
            stats.append("\n Part of " + PTPBot.getSkype().getConversations().size() + " conversations");
            stats.append("\n Uptime " + DurationFormatUtils.formatPeriod(start, finish, "d 'days,' H 'hours,' m 'mins and' s 'seconds'"));
            group.sendMessage(stats.toString());

        }
    }

}
