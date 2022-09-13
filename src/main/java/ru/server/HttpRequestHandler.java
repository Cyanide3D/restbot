package ru.server;

import org.apache.commons.lang3.StringUtils;
import ru.server.request.HttpRequest;
import ru.server.response.HttpResponse;

public interface HttpRequestHandler {

    void handle(HttpRequest request, HttpResponse response);

}
