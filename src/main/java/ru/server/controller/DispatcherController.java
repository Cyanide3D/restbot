package ru.server.controller;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import ru.bot.Bot;
import ru.server.constants.HttpMethod;
import ru.server.controller.annotations.*;
import ru.server.exception.HttpException;
import ru.server.exception.NotFoundHttpException;
import ru.server.exception.UnsupportedHttpMethodException;
import ru.server.request.HttpRequest;
import ru.server.request.Params;
import ru.server.response.HttpResponse;
import ru.server.util.UrlUtil;
import ru.server.view.ViewResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class DispatcherController {

    private final List<Object> controllers;
    private final Bot bot;

    public DispatcherController(String controllersPath, Bot bot) {
        this.bot = bot;
        this.controllers = new ArrayList<>();
        Reflections reflections = new Reflections(controllersPath);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            try {
                this.controllers.add(controller.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Object handleRequestByController(HttpRequest request, HttpResponse response) {
        String[] pathParts = UrlUtil.cutSlash(request.getPath().split("\\?")[0]).split("/");
        for (Map.Entry<Object, Integer> entry : getSortedControllersByPathMatches(pathParts)) {
            String p = StringUtils.join(pathParts, "/", entry.getValue(), pathParts.length);
            if (p.isEmpty()) p = "/";
            for (Method method : entry.getKey().getClass().getDeclaredMethods()) {
                try {
                    if (UrlUtil.cutSlash(getMethodPath(request.getMethod(), method)).equals(p)) {
                        return method.invoke(entry.getKey(), parameters(method, request, response, new Params(request.getPath()), bot));
                    }
                } catch (NullPointerException | IllegalAccessException ignore) {
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof HttpException) throw (HttpException) e.getCause();
                    e.printStackTrace();
                }
            }
        }

        throw new NotFoundHttpException();
    }

    private Object[] parameters(Method method, Object... params) {
        List<Object> result = new ArrayList<>();
        for (Class<?> methodParam : method.getParameterTypes()) {
            result.add(Arrays.stream(params).filter(p -> p.getClass().isAssignableFrom(methodParam)).findFirst().orElse(null));
        }
        return result.toArray();
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

    private List<Map.Entry<Object, Integer>> getSortedControllersByPathMatches(String[] pathParts) {
        Map<Object, Integer> map = new HashMap<>();
        for (Object controller : controllers) {
            Controller annotation = controller.getClass().getAnnotation(Controller.class);
            String[] annotationPath = UrlUtil.cutSlash(annotation.path()).split("/");
            if (annotationPath.length > pathParts.length) continue;
            int index = 0;
            for (; index < Math.min(annotationPath.length, pathParts.length); index++) {
                if (!pathParts[index].equals(annotationPath[index])) break;
            }

            map.put(controller, index);
        }
        return map.entrySet().stream().sorted((e1, e2) -> e2.getValue() - e1.getValue()).collect(Collectors.toList());
    }
}
