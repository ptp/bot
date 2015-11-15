package me.imsean.ptpbot.api.settings;

import com.jayway.jsonpath.JsonPath;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by sean on 11/15/15.
 */
public class ConfigManager {

    private File config;

    public void loadConfig(String file) {
        this.config = new File(file);
    }

    public String getString(String path) {
        try {
            return (String) JsonPath.read(this.config, path);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't find path: " + path, e);
        }
    }

    public List<String> get(String path) {
        try {
            return JsonPath.read(this.config, path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read config file", e);
        }
    }

}
