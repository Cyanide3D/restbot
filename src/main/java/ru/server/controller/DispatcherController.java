package ru.server.controller;

import ru.server.request.HttpRequest;

import java.lang.reflect.InvocationTargetException;

public interface DispatcherController {

    Object handleRequestByController(HttpRequest request) throws InvocationTargetException, IllegalAccessException;
    void setArgs(ControllerArgs args);

}
