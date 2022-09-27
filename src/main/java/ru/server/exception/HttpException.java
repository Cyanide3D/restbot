package ru.server.exception;

import ru.server.Headers;
import ru.server.constants.HttpStatus;
import ru.server.constants.ContentType;
import ru.server.response.HttpResponse;

public class HttpException extends RuntimeException {

    private final HttpStatus status;

    public HttpException(HttpStatus status) {
        this.status = status;
    }

    public void errorResponse(HttpResponse response) {
        errorResponse(response, status);
    }

    public static void errorResponse(HttpResponse response, HttpStatus status) {
        response.setStatus(status);
        fillResponse(response);
    }

    private static void fillResponse(HttpResponse response) {
        response.setHeaders(new Headers());
        response.addHeader("Connection", "Close");
        response.addHeader("Content-Type", ContentType.JSON);

        response.setBody(String.format("{\"code\":\"%d\",\"message\":\"%s\"}", response.getStatus().getStatusCode(), response.getStatus().getStatus()));
    }


}
