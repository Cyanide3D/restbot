package ru.server.view.data;

import java.util.HashMap;
import java.util.Map;

public class ModelViewData implements ViewData {

    private final Map<String, Object> data;

    public ModelViewData() {
        this.data = new HashMap<>();
    }

    @Override
    public void put(String key, Object data) {
        this.data.put(key, data);
    }

    @Override
    public Object get(String key) {
        return data.get(key);
    }
}
