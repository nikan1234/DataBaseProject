package com.railway.ui.window.auth;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsStorage {
    private static final String settingsFile = "settings.json";
    private static volatile SettingsStorage instance = null;

    private String databaseAddress;
    private int databasePort;

    private SettingsStorage() {
        JSONParser jsonParser = new JSONParser();

        try(FileReader reader = new FileReader(settingsFile)) {
            JSONObject object = (JSONObject) jsonParser.parse(reader);
            databaseAddress = (String) object.get("Database address");
            databasePort = ((Long) object.get("Database port")).intValue();

        }
        catch (final IOException | ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    public static SettingsStorage getInstance() {
        SettingsStorage local_instance = instance;
        if (instance == null) {
            synchronized (SettingsStorage.class) {
                local_instance = instance;
                if (instance == null) {
                    instance = local_instance = new SettingsStorage();
                }
            }
        }
        return local_instance;
    }

    public String getDatabaseAddress() {
        return databaseAddress;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public void setDatabaseAddress(String address) {
        this.databaseAddress = address;
    }

    public void setDatabasePort(int port) {
        this.databasePort = port;
    }

    public void saveSettings() {
        JSONObject settings = new JSONObject();
        settings.put("Database address", databaseAddress);
        settings.put("Database port", databasePort);
        try (FileWriter file = new FileWriter("settings.json")) {

            file.write(settings.toJSONString());
            file.flush();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
