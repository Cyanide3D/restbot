package ru.server;

import ru.Configuration;
import ru.server.request.HttpRequest;
import ru.server.response.HttpResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Server {

    private final HttpRequestHandler handler;
    private final Configuration configuration;

    public Server(Configuration configuration, HttpRequestHandler handler) {
        this.configuration = configuration;
        this.handler = handler;
    }


    public void start() {
        try {
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(configuration.getServerIp(), Integer.parseInt(configuration.getServerPort())));

            while (true) {
                Future<AsynchronousSocketChannel> client = serverSocketChannel.accept();
                try {
                    AsynchronousSocketChannel socketChannel = client.get(60, TimeUnit.SECONDS);
                    String req = readRequest(socketChannel);

                    HttpRequest httpRequest = new HttpRequest(req);
                    HttpResponse httpResponse = new HttpResponse();

                    handler.handle(httpRequest, httpResponse);

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
