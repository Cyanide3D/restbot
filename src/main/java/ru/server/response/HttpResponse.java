package ru.server.response;

import ru.server.constants.ContentType;
import ru.server.Headers;
import ru.server.constants.HttpStatus;
import ru.server.view.ViewResolver;

public class HttpResponse {

    private Headers headers;
    private final static String NEW_LINE = "\r\n";

    private String body = "";
    private HttpStatus status = HttpStatus.OK;

    public HttpResponse() {
        this.headers = new Headers();
        headers.addHeader("Connection", "Close");
        headers.addHeader("Content-Type", ContentType.JSON);
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public void addHeader(String header, String value) {
        headers.addHeader(header, value);
    }

    public void setBody(String body) {
        headers.addHeader("Content-Length", String.valueOf(body.length()));
        this.body = body;
    }


    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String message() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 ").append(status.getStatusCode()).append(" ").append(status.getStatus()).append(NEW_LINE);

        headers.getHeaders().forEach((k, v) -> {
            stringBuilder.append(k).append(": ").append(v).append(NEW_LINE);
        });

        return stringBuilder.append(NEW_LINE).append(body).toString();
    }

}
