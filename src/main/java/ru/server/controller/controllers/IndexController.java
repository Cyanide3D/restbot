package ru.server.controller.controllers;

import ru.bot.Bot;
import ru.server.FormDataParser;
import ru.server.constants.ContentType;
import ru.server.controller.annotations.Controller;
import ru.server.controller.annotations.Get;
import ru.server.controller.annotations.PathArg;
import ru.server.controller.annotations.Post;
import ru.server.exception.BadRequestHttpException;
import ru.server.exception.UnsupportedMTHttpException;
import ru.server.request.HttpRequest;
import ru.server.response.HttpResponse;
import ru.server.view.data.ModelViewData;
import ru.server.view.data.ViewData;

import java.text.MessageFormat;

@Controller
public class IndexController {

//    @Post()
//    public String handle(Bot bot, HttpResponse response, HttpRequest request) {
//        String header = request.getHeaders().getHeader("Content-Type");
//        if (header == null) throw new UnsupportedMTHttpException();
//        if (!header.contains(ContentType.FORM)) throw new UnsupportedMTHttpException();
//
//        FormDataParser formDataParser = new FormDataParser(request.getBody());
//        String title = formDataParser.getDataByKey("notetitle");
//        String message = formDataParser.getDataByKey("message");
//        if (message == null || title == null) throw new BadRequestHttpException();
//
//        bot.sendMessage(MessageFormat.format("*{0}*\n{1}", title, message));
//        return "{\"status\":\"Vse zaebis\"}";
//    }

    @Get()
    public String index(ModelViewData data) {
        data.put("main", "hello world!");
        data.put("how", "qweqweqwe");
        return "index/index";
    }

    @Get(path = "/{path}")
    public String about(@PathArg("path") String pathPart) {
        System.out.println(pathPart);
        return "index/about";
    }

}
