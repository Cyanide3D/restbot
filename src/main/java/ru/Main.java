package ru;

import ru.bot.Bot;
import ru.server.FormDataParser;
import ru.server.Server;
import ru.server.exception.BadRequestHttpException;
import ru.server.exception.HttpException;
import ru.server.exception.UnsupportedMTHttpException;
import ru.server.constants.ContentType;

import java.text.MessageFormat;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        Bot bot = new Bot(configuration);
        bot.start();

        Server server = new Server(configuration, (request, response) -> {
            try {
                String header = request.getHeaders().getHeader("Content-Type");
                if (header == null) throw new UnsupportedMTHttpException();
                if (!header.contains(ContentType.FORM)) throw new UnsupportedMTHttpException();

                FormDataParser formDataParser = new FormDataParser(request.getBody());
                String title = formDataParser.getDataByKey("title");
                String message = formDataParser.getDataByKey("message");
                if (message == null || title == null) throw new BadRequestHttpException();

                bot.sendMessage(MessageFormat.format("*{0}*\n{1}", title, message));
                response.setBody("{\"status\":\"Vse zaebis\"}");
            } catch (HttpException e) {
                e.errorResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        server.start();
    }

}
