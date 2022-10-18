package ru.server.controller;

import ru.server.controller.annotations.PathArg;

import java.lang.reflect.Parameter;
import java.util.*;

public class ControllerArgs {

    private final List<Object> methodArgs;
    private final HashMap<String, String> pathsArg;

    public ControllerArgs() {
        this.methodArgs = new ArrayList<>();
        this.pathsArg = new HashMap<>();
    }

    public ControllerArgs(Object... args) {
        this.methodArgs = new ArrayList<>(Arrays.asList(args));
        this.pathsArg = new HashMap<>();
    }

    public void addPathArg(String key, String value) {
        pathsArg.put(key, value);
    }

    public void addMethodArg(Object object) {
        Objects.requireNonNull(object);
        methodArgs.add(object);
    }

    public Object getArgByType(Parameter parameter) {
        Class<?> type = parameter.getType();
        if (type.equals(String.class)) {
            PathArg annotation = parameter.getAnnotation(PathArg.class);
            if (annotation == null) return null;
            return pathsArg.get(annotation.value());
        }
        return methodArgs.stream().filter(p -> p.getClass().isAssignableFrom(type)).findFirst().orElse(null);
    }

}
