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
        response.setStatus(status);
        response.setHeaders(new Headers());
        response.addHeader("Connection", "Close");
        response.addHeader("Content-Type", ContentType.JSON);

        response.setBody(String.format("{\"code\":\"%d\",\"message\":\"%s\"}", status.getStatusCode(), status.getStatus()));
    }

}
