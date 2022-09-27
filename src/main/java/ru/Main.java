package ru;

import ru.bot.Bot;
import ru.server.Server;
import ru.server.configure.Configuration;
import ru.server.controller.DispatcherController;
import ru.server.view.HtmlViewResolver;
import ru.server.view.JsonViewResolver;
import ru.server.view.ViewResolver;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        Bot bot = new Bot(configuration);
        bot.start();

        ViewResolver jsonViewResolver = new JsonViewResolver();
        ViewResolver htmlViewResolver = new HtmlViewResolver();
        htmlViewResolver.setPostfix(".html");
        htmlViewResolver.setTemplatePath("templates");

        DispatcherController controllers = new DispatcherController(configuration.getControllersPath(), bot);
        Server server = new Server(configuration, controllers, htmlViewResolver);
        server.start();
    }

}
