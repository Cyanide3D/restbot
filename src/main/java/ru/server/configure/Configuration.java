package ru.server.configure;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Configuration {

    private final Properties properties;

    public Configuration() {
        this.properties = new Properties();
        try {
            properties.load(Files.newInputStream(Path.of("settings.properties")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getControllersPath() {
        return properties.getProperty("CONTROLLER_PATH");
    }

    public String getBotToken() {
        return properties.getProperty("TOKEN");
    }

    public String getServerIp() {
        return properties.getProperty("IP");
    }

    public String getServerPort() {
        return properties.getProperty("PORT");
    }

    public String getTelegramChatId() {
        return properties.getProperty("CHAT_ID");
    }

}
