package ru.server;

import ru.server.configure.Configuration;
import ru.server.constants.ContentType;
import ru.server.constants.HttpStatus;
import ru.server.controller.DispatcherController;
import ru.server.exception.HttpException;
import ru.server.request.HttpRequest;
import ru.server.response.HttpResponse;
import ru.server.view.ViewResolver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Server {

    private final Configuration configuration;
    private final DispatcherController controller;
    private final ViewResolver viewResolver;

    public Server(Configuration configuration, DispatcherController controller, ViewResolver viewResolver) {
        this.controller = controller;
        this.configuration = configuration;
        this.viewResolver = viewResolver;
    }


    public void start() {
        try {
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(configuration.getServerIp(), Integer.parseInt(configuration.getServerPort())));
            while (true) {
                Future<AsynchronousSocketChannel> client = serverSocketChannel.accept();
                try {
                    AsynchronousSocketChannel socketChannel = client.get(60, TimeUnit.SECONDS);
                    HttpResponse httpResponse = new HttpResponse();
                    try {
                        String req = readRequest(socketChannel);
                        HttpRequest httpRequest = new HttpRequest(req);
                        viewResolver.handle(controller.handleRequestByController(httpRequest, httpResponse));
                    } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                        HttpException.errorResponse(httpResponse, HttpStatus.BAD_REQUEST);
                        e.printStackTrace();
                    } catch (HttpException e){
                        e.errorResponse(httpResponse);
                    } catch (Exception e) {
                        HttpException.errorResponse(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                        e.printStackTrace();
                    }
                    httpResponse.setBody(viewResolver.getTemplateAsText());
                    httpResponse.addHeader("Content-Type", viewResolver.getContentType());
                    socketChannel.write(ByteBuffer.wrap(httpResponse.message().getBytes())).get();
                    socketChannel.close();
                } catch (Exception e) {
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
