package ru.server.controller;

import ru.server.constants.HttpMethod;
import ru.server.controller.annotations.*;
import ru.server.exception.UnsupportedHttpMethodException;
import ru.server.util.UrlUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControllerTree {

    private String path;
    private final List<Object> controllers;
    private final List<ControllerTree> children;


    public ControllerTree() {
        this.children = new ArrayList<>();
        this.controllers = new ArrayList<>();
    }

    public ControllerTree(String path) {
        this.path = path;
        this.controllers = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public ControllerData findController(String path, HttpMethod method) {
        return findController(new ArrayList<>(Arrays.asList(UrlUtil.asArray(path))), method);
    }

    private ControllerData findController(List<String> path, HttpMethod method) {
        if (path.isEmpty()) {
            return findBetweenControllers("/", method);
        }

        String p = String.join("/", path);
        String ptf = path.remove(0);
        for (ControllerTree child : children) {
            if (child.getPath().equals(ptf)) {
                ControllerData o = child.findController(path, method);
                if (o != null)
                    return o;
            }
        }

        return findBetweenControllers(p, method);
    }

    private ControllerData findBetweenControllers(String path, HttpMethod method) {
        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getMethods();
            for (Method m : methods) {
                try {
                    String methodPath = getMethodPath(method, m);
                    if (isEqualsPaths(path, methodPath)) {
                        return new ControllerData(controller, m, methodPath, path);
                    }
                } catch (UnsupportedHttpMethodException | NullPointerException e) {
                }
            }
        }
        return null;
    }

    private boolean isEqualsPaths(String cp, String mp) {
        List<String> s1 = List.of(UrlUtil.asArray(cp));
        List<String> s2 = List.of(UrlUtil.asArray(mp));

        if (s1.size() != s2.size())
            return false;

        for (int i = 0; i < s1.size(); i++) {
            if (!s1.get(i).equals(s2.get(i))) {
                if (s2.get(i).startsWith("{") && s2.get(i).endsWith("}"))
                    continue;
                return false;
            }
        }

        return true;
    }

    private String getMethodPath(HttpMethod httpMethod, Method method) {
        switch (httpMethod) {
            case GET -> {
                return method.getAnnotation(Get.class).path();
            }
            case POST -> {
                return method.getAnnotation(Post.class).path();
            }
            case PATCH -> {
                return method.getAnnotation(Patch.class).path();
            }
            case DELETE -> {
                return method.getAnnotation(Delete.class).path();
            }
        }

        throw new UnsupportedHttpMethodException();
    }


    public void add(Object controller) {
        Controller annotation = controller.getClass().getAnnotation(Controller.class);
        add(controller, new ArrayList<>(Arrays.asList(UrlUtil.asArray(annotation.path()))));
    }

    private void add(Object controller, List<String> path) {
        if (path.isEmpty()) {
            controllers.add(controller);
            return;
        }

        String p = path.remove(0);
        for (ControllerTree child : children) {
            if (child.getPath().equals(p)) {
                child.add(controller, path);
                return;
            }
        }
        ControllerTree child = new ControllerTree(p);
        children.add(child);
        child.add(controller, path);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
