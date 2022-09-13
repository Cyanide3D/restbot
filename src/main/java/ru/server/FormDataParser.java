package ru.server;

import ru.server.exception.BadRequestHttpException;

import java.util.HashMap;
import java.util.Map;

public class FormDataParser {

    Map<String, String> data;

    public FormDataParser(String message) {
        data = new HashMap<>();
        try {
            String[] parts = message.split("&");
            for (String part : parts) {
                String[] p = part.split("=");
                data.put(p[0], p[1]);
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            throw new BadRequestHttpException();
        }
    }

    public String getDataByKey(String key) {
        return data.get(key);
    }

}
