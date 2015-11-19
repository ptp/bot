package me.imsean.ptpbot.commands;

import com.jayway.jsonpath.JsonPath;
import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import me.imsean.ptpbot.api.command.Command;
import me.imsean.ptpbot.api.command.CommandCategory;
import me.imsean.ptpbot.api.util.ASCIIArtGen;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sean on 11/15/15.
 */
public class ImageCommand extends Command {

    public ImageCommand() {
        super(SkypeUserRole.ADMIN, "Convert images to ascii text", CommandCategory.FUN, "image");
    }

    @Override
    public void onCommand(SkypeMessage message, SkypeConversation group, SkypeUser user, String[] args) {
        if(args.length == 0) {
            group.sendMessage(user.getUsername() + " - Usage: image (url)");
        }
        if(args.length > 0) {
            if(args[0].isEmpty()) {
                group.sendMessage(user.getUsername() + " - Usage: image (url)");
                return;
            }
            SkypeMessage art = group.sendMessage("Converting...");
            Thread thread = new Thread(() -> {
                StringBuilder html = new StringBuilder();
                for(String arg : args) {
                    html.append(arg).append(" ");
                }

                Document doc = Jsoup.parse(html.toString());
                Element link = doc.select("a").first();
                String href = link.attr("href");
                try {
                    URL website = new URL(href);
                    ASCIIArtGen gen = new ASCIIArtGen(new Font("SansSerif", Font.PLAIN, 24));
                    BufferedImage image = ImageIO.read(website);
                    String url = paste(gen.convert(image, 200));
                    art.edit(Chat.link(RandomStringUtils.randomAlphabetic(24) + ".ascii", url));
                } catch (MalformedURLException e) {
                    art.edit("Invalid URL");
                } catch (IOException e) {
                    art.edit("Error getting file");
                }
            });
            thread.start();
        }
    }

    public synchronized static String paste(String urlParameters) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://www.hastebin.com/documents");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String key = JsonPath.read(rd.readLine(), "$.key");
            return "http://www.hastebin.com/" + key;
        } catch (Exception ex) {
            return null;
        } finally {
            if (connection == null) { return null; }
            connection.disconnect();
        }
    }

}
