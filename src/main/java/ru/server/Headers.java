package ru.server;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Headers {

    private final Map<String, String> headers;

    public Headers() {
        this.headers = new HashMap<>();
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    public int amount() {
        return headers.size();
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "Headers{" +
                "headers=" + headers +
                '}';
    }
}
