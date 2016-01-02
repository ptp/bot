package me.imsean.ptpbot.commands;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 11/28/15.
 */
public class UrbanCommand extends Command {

    private HashMap<String, String> cache = new HashMap<>();

    public UrbanCommand() {
        super(SkypeUserRole.USER, "Get the definition of a word", CommandCategory.INFORMATIVE, "urban", "ud");
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(args.length == 0) {
            group.sendMessage(user.getUsername() + " - Usage: urban (word)");
        }
        if(args.length > 0) {
            StringBuilder words = new StringBuilder();
            for(String arg : args) {
                words.append(arg.trim()).append(" ");
            }
            String word = words.toString().trim();
            if(word.isEmpty()) {
                group.sendMessage(user.getUsername() + " - Usage: urban (word)");
                return;
            }
            if(cache.containsKey(word)) {
                group.sendMessage(cache.get(word));
                return;
            }
            try {
                String ud = Jsoup.connect("http://api.urbandictionary.com/v0/define?term=" + word).ignoreContentType(true).execute().body();
                Object json = Configuration.defaultConfiguration().jsonProvider().parse(ud);

                String resultType = JsonPath.read(json, "$.result_type");
                if(resultType.equalsIgnoreCase("no_results")) {
                    group.sendMessage(user.getUsername() + " - Cannot define " + Chat.bold(word));
                    return;
                }
                List results = JsonPath.read(json, "$.list");
                HashMap result = JsonPath.read(json, "$.list[0]");
                String def = bbcode(JsonPath.read(result, "$.definition"));
                String perma = JsonPath.read(result, "$.permalink");
                String resList = new URL(perma).getHost();

                StringBuilder definition = new StringBuilder();
                definition.append("Definition of ").append(Chat.bold(word)).append(" -").append("\n");
                definition.append(def.trim()).append("\n");
                definition.append(Chat.bold("[")).append(Chat.link("Permalink", perma)).append(Chat.bold("]"));
                group.sendMessage(definition.toString());
                cache.put(word, definition.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String bbcode(String text) {
        String html = text;

        Map<String,String> bbMap = new HashMap<>();

        bbMap.put("(\r\n|\r|\n|\n\r)", "<br/>");
        //bbMap.put("\\[word\\](.+?)\\[/word\\]", Chat.link("$1", "http://$1.urbanup.com"));
        bbMap.put("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
        bbMap.put("\\[i\\](.+?)\\[/i\\]", "<span style='font-style:italic;'>$1</span>");
        bbMap.put("\\[u\\](.+?)\\[/u\\]", "<span style='text-decoration:underline;'>$1</span>");
        bbMap.put("\\[h1\\](.+?)\\[/h1\\]", "<h1>$1</h1>");
        bbMap.put("\\[h2\\](.+?)\\[/h2\\]", "<h2>$1</h2>");
        bbMap.put("\\[h3\\](.+?)\\[/h3\\]", "<h3>$1</h3>");
        bbMap.put("\\[h4\\](.+?)\\[/h4\\]", "<h4>$1</h4>");
        bbMap.put("\\[h5\\](.+?)\\[/h5\\]", "<h5>$1</h5>");
        bbMap.put("\\[h6\\](.+?)\\[/h6\\]", "<h6>$1</h6>");
        bbMap.put("\\[quote\\](.+?)\\[/quote\\]", "<blockquote>$1</blockquote>");
        bbMap.put("\\[p\\](.+?)\\[/p\\]", "<p>$1</p>");
        bbMap.put("\\[p=(.+?),(.+?)\\](.+?)\\[/p\\]", "<p style='text-indent:$1px;line-height:$2%;'>$3</p>");
        bbMap.put("\\[center\\](.+?)\\[/center\\]", "<div align='center'>$1");
        bbMap.put("\\[align=(.+?)\\](.+?)\\[/align\\]", "<div align='$1'>$2");
        bbMap.put("\\[color=(.+?)\\](.+?)\\[/color\\]", "<span style='color:$1;'>$2</span>");
        bbMap.put("\\[size=(.+?)\\](.+?)\\[/size\\]", "<span style='font-size:$1;'>$2</span>");
        bbMap.put("\\[img\\](.+?)\\[/img\\]", "<img src='$1' />");
        bbMap.put("\\[img=(.+?),(.+?)\\](.+?)\\[/img\\]", "<img width='$1' height='$2' src='$3' />");
        bbMap.put("\\[email\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$1</a>");
        bbMap.put("\\[email=(.+?)\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$2</a>");
        bbMap.put("\\[url\\](.+?)\\[/url\\]", "<a href='$1'>$1</a>");
        bbMap.put("\\[url=(.+?)\\](.+?)\\[/url\\]", "<a href='$1'>$2</a>");
        bbMap.put("\\[youtube\\](.+?)\\[/youtube\\]", "<object width='640' height='380'><param name='movie' value='http://www.youtube.com/v/$1'></param><embed src='http://www.youtube.com/v/$1' type='application/x-shockwave-flash' width='640' height='380'></embed></object>");
        bbMap.put("\\[video\\](.+?)\\[/video\\]", "<video src='$1' />");

        for (Map.Entry entry: bbMap.entrySet()) {
            html = html.replaceAll(entry.getKey().toString(), entry.getValue().toString());
        }

        return html;
    }

}
