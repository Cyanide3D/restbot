package ru.server.request;

import java.util.HashMap;
import java.util.Map;

public class Params {

    private final Map<String, String> params;

    public Params(String path) {
        this.params = new HashMap<>();
        String[] s = path.split("\\?");
        if (s.length > 1) {
            for (String param : s[1].split("&")) {
                String[] paramParts = param.split("=");
                this.params.put(paramParts[0], paramParts[1]);
            }
        }
    }

    public boolean isHaveParams() {
        return !params.isEmpty();
    }

    public String getParam(String param) {
        return params.get(param);
    }

    @Override
    public String toString() {
        return "Params{" +
                "params=" + params +
                '}';
    }
}
