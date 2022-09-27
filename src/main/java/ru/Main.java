package ru;

import ru.bot.Bot;
import ru.server.Server;
import ru.server.configure.Configuration;
import ru.server.controller.DispatcherController;
import ru.server.view.JsonViewResolver;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        Bot bot = new Bot(configuration);
        bot.start();

        JsonViewResolver jsonViewResolver = new JsonViewResolver();

        DispatcherController controllers = new DispatcherController(configuration.getControllersPath(), bot);
        Server server = new Server(configuration, controllers, jsonViewResolver);
        server.start();
    }

}
