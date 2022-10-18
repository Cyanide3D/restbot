package ru.server.view.data;

public interface ViewData {

    void put(String key, Object data);
    Object get(String key);

}
