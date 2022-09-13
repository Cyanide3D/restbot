package ru.server.request;

import ru.server.Headers;
import ru.server.constants.HttpMethod;

public class HttpRequest {

    private final Headers headers;
    private final String body;
    private final HttpMethod method;

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

        this.method = HttpMethod.valueOf(headLines[0].split(" ")[0]);
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
