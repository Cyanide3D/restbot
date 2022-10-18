package ru;

import ru.bot.Bot;
import ru.server.Server;
import ru.server.configure.Configuration;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        Bot bot = new Bot(configuration);
        bot.start();

        Server server = new Server(configuration, bot);
        server.start();
    }

}
