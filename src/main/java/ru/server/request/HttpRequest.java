package ru.server.request;

import ru.server.Headers;
import ru.server.constants.HttpMethod;

public class HttpRequest {

    private final Headers headers;
    private final String body;
    private final HttpMethod method;
    private final String path;
    private final String protocol;

    private final static String DELIMITER = "\r\n\r\n";
    private final static String NEW_LINE = "\r\n";
    private final static String HEADER_DELIMITER = ":";

    public HttpRequest(String request) {
        headers = new Headers();

        String[] parts = request.split(DELIMITER);
        String headers = parts[0];
        this.body = parts[1];

        String[] headLines = headers.split(NEW_LINE);
        for (int i = 1; i < headLines.length; i++) {
            String[] headerParts = headLines[i].split(HEADER_DELIMITER, 2);
            this.headers.addHeader(headerParts[0].trim(), headerParts[1].trim());
        }

        String[] fl = headLines[0].split(" ");
        this.method = HttpMethod.valueOf(fl[0]);
        this.path = fl[1];
        this.protocol = fl[2];
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
