package ru.server;

import ru.bot.Bot;
import ru.server.configure.Configuration;
import ru.server.constants.HttpStatus;
import ru.server.controller.ControllerArgs;
import ru.server.controller.DispatcherController;
import ru.server.controller.DispatcherControllerImpl;
import ru.server.exception.HttpException;
import ru.server.request.HttpRequest;
import ru.server.request.Params;
import ru.server.response.HttpResponse;
import ru.server.view.HtmlViewResolver;
import ru.server.view.TemplateViewResolver;
import ru.server.view.data.ModelViewData;
import ru.server.view.data.ViewData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Server {

    private final Configuration configuration;
    private final Bot bot;

    public Server(Configuration configuration, Bot bot) {
        this.configuration = configuration;
        this.bot = bot;
    }


    public void start() {
        try {
            DispatcherController controller = new DispatcherControllerImpl(configuration.getControllersPath()); //TODO controller and resolver should be created somewhere out (DI or config)
            ViewData viewData = new ModelViewData();
//            TemplateViewResolver viewResolver = new HtmlViewResolver(new HtmlInterpreterImpl(), viewData);
//            viewResolver.setTemplatePath("templates");


            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(configuration.getServerIp(), Integer.parseInt(configuration.getServerPort())));
            while (true) {
                Future<AsynchronousSocketChannel> client = serverSocketChannel.accept();
                HttpResponse httpResponse = new HttpResponse();
                try (AsynchronousSocketChannel socketChannel = client.get()) {
                    HttpRequest httpRequest = new HttpRequest(readRequest(socketChannel));
                    controller.setArgs(new ControllerArgs(new Params(httpRequest.getPath()), httpRequest, httpResponse, bot, viewData));

//                    viewResolver.handle(controller.handleRequestByController(httpRequest));
//                    httpResponse.setBody(viewResolver.getTemplateAsText());
//                    httpResponse.addHeader("Content-Type", viewResolver.getContentType());

                    socketChannel.write(ByteBuffer.wrap(httpResponse.message().getBytes())).get();
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                    HttpException.errorResponse(httpResponse, HttpStatus.BAD_REQUEST);
                    e.printStackTrace();
                } catch (HttpException e) {
                    e.errorResponse(httpResponse);
                } catch (Exception e) {
                    HttpException.errorResponse(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readRequest(AsynchronousSocketChannel socketChannel) throws InterruptedException, ExecutionException {
        StringBuilder stringBuilder = new StringBuilder();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int readed = 1024;
        while (socketChannel != null && socketChannel.isOpen() && readed == 1024) {
            socketChannel.read(buffer).get();
            readed = buffer.position();
            buffer.flip();
            stringBuilder.append(new String(buffer.array()));
            buffer.clear();
        }
        return stringBuilder.toString();
    }

}
