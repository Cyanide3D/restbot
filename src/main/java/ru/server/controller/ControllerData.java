package ru.server.controller;

import java.lang.reflect.Method;

public class ControllerData {

    private Object controller;
    private Method method;
    private String methodPath;
    private String relPath;

    public ControllerData(Object controller, Method method, String methodPath, String relPath) {
        this.controller = controller;
        this.method = method;
        this.methodPath = methodPath;
        this.relPath = relPath;
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }

    public String getMethodPath() {
        return methodPath;
    }


    public String getRelPath() {
        return relPath;
    }
}
