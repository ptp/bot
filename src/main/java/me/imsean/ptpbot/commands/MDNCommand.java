package me.imsean.ptpbot.commands;

import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by sean on 12/8/15.
 */
public class MDNCommand extends Command {

    public MDNCommand() {
        super(SkypeUserRole.USER, "Lookup queries from the MDN Database", CommandCategory.INFORMATIVE, "mdn");
    }


    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(args.length == 0) {
            group.sendMessage(user.getUsername() + " - Usage: mdn (term)");
        }
        if(args.length > 0) {
            StringBuilder term = new StringBuilder();
            for(String arg : args) {
                term.append(arg.trim()).append(" ");
            }

            SkypeMessage mdn = group.sendMessage("Searching for definition of " + Chat.bold(term.toString().trim()) + "...");

            Thread thread = new Thread(() -> {
                try {
                    Document doc = Jsoup.connect("http://mdn.io/" + term.toString()).followRedirects(true).userAgent("Mozilla").get();
                    Element article = doc.getElementById("wikiArticle");
                    Element definition = null;
                    try {
                        definition = article.select("p:not(:empty)").first();
                        if(definition.parent().hasClass("overheadIndicator")) {
                            definition = article.select("p:not(:empty)").eq(2).first();
                        }
                    } catch (NullPointerException e) {
                        mdn.edit("Could not define " + Chat.bold(term.toString().trim()));
                        return;
                    }
                    String definitionText = definition.text();

                    StringBuilder fullDefinition = new StringBuilder();
                    fullDefinition.append("MDN Definition of ").append(Chat.bold(term.toString().trim())).append(" - \n");
                    if(definitionText.trim().isEmpty()) {
                        Element summary = doc.getElementById("Summary");
                        definitionText = summary.nextElementSibling().text();
                    }
                    fullDefinition.append(definitionText);
                    fullDefinition.append("\n").append(Chat.bold("[")).append(Chat.link("Permalink", doc.location())).append(Chat.bold("]"));
                    mdn.edit(fullDefinition.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}
