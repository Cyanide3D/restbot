package ru.server.controller;

import org.reflections.Reflections;
import ru.server.controller.annotations.Controller;
import ru.server.exception.NotFoundHttpException;
import ru.server.request.HttpRequest;
import ru.server.util.UrlUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class DispatcherControllerImpl implements DispatcherController {

    private final ControllerTree tree;
    private ControllerArgs args;

    public DispatcherControllerImpl(String controllersPath) {
        this.tree = new ControllerTree();
        this.args = new ControllerArgs();

        Reflections reflections = new Reflections(controllersPath);
        reflections.getTypesAnnotatedWith(Controller.class).forEach(c -> {
            try {
                tree.add(c.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ignored) {
            }
        });
    }

    @Override
    public Object handleRequestByController(HttpRequest request) throws InvocationTargetException, IllegalAccessException {
        ControllerData data = tree.findController(request.getPath(), request.getMethod());

        if (data == null)
            throw new NotFoundHttpException();

        Method method = data.getMethod();
        return method.invoke(data.getController(), parameters(method, data));
    }

    @Override
    public void setArgs(ControllerArgs args) {
        this.args = args;
    }

    private Object[] parameters(Method method, ControllerData controllerData) {
        String[] methodPath = UrlUtil.asArray(controllerData.getMethodPath());
        String[] relPath = UrlUtil.asArray(controllerData.getRelPath());

        for (int i = 0; i < methodPath.length; i++) {
            if (methodPath[i].startsWith("{") && methodPath[i].endsWith("}")) {
                args.addPathArg(methodPath[i].substring(1, methodPath[i].length() - 1).trim(), relPath[i]);
            }
        }

        List<Object> result = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            result.add(args.getArgByType(parameter));
        }
        return result.toArray();

    }

}
