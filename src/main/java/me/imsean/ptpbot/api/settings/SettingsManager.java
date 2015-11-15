package me.imsean.ptpbot.api.settings;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by sean on 10/17/15.
 */
@Deprecated
public class SettingsManager {

    private String botUsername, botPassword;
    private String dUsername, dPassword;
    private String dbHost, dbDatabase, dbUsername, dbPassword;

    private String discordLink;

    public SettingsManager() throws IOException, ParserConfigurationException, SAXException {
        File configXML = new File("config.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document config = dBuilder.parse(configXML);

        config.getDocumentElement().normalize();

        NodeList dbDetails = config.getElementsByTagName("database");
        NodeList botDetails = config.getElementsByTagName("skype");
        NodeList simpleDetails = config.getElementsByTagName("misc");
//        NodeList discordDetails = config.getElementsByTagName("discordbot");
//
//        for(int i = 0; i < discordDetails.getLength(); i++) {
//            Node nodes = discordDetails.item(i);
//
//            if(nodes.getNodeType() == Node.ELEMENT_NODE) {
//                Element el = (Element) nodes;
//
//                this.dUsername = el.getElementsByTagName("user").item(0).getTextContent();
//                this.dPassword = el.getElementsByTagName("pass").item(0).getTextContent();
//            }
//        }

        for(int i = 0; i < simpleDetails.getLength(); i++) {
            Node nodes = simpleDetails.item(i);

            if(nodes.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nodes;

                this.discordLink = el.getElementsByTagName("discord").item(0).getTextContent();
            }
        }

        for(int i = 0; i < dbDetails.getLength(); i++) {
            Node nodes = dbDetails.item(i);

            if(nodes.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nodes;

                this.dbHost = el.getElementsByTagName("host").item(0).getTextContent();
                this.dbDatabase = el.getElementsByTagName("name").item(0).getTextContent();
                this.dbUsername = el.getElementsByTagName("user").item(0).getTextContent();
                this.dbPassword = el.getElementsByTagName("pass").item(0).getTextContent();
            }
        }

        for(int i = 0; i < botDetails.getLength(); i++) {
            Node nodes = botDetails.item(i);

            if(nodes.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nodes;

                this.botUsername = el.getElementsByTagName("user").item(0).getTextContent();
                this.botPassword = el.getElementsByTagName("pass").item(0).getTextContent();
            }
        }
    }


    public String getBotUsername() {
        return this.botUsername;
    }

    public String getBotPassword() {
        return this.botPassword;
    }

    public String getDbHost() {
        return this.dbHost;
    }

    public String getDbDatabase() {
        return this.dbDatabase;
    }

    public String getDbUsername() {
        return this.dbUsername;
    }

    public String getDbPassword() {
        return this.dbPassword;
    }

    public String getDiscordLink() { return this.discordLink; }

    public String getDiscordUsername() { return this.dUsername; }

    public String getDiscordPassword() { return this.dPassword; }



}
