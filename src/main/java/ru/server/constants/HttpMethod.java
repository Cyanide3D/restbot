package ru.server.constants;

import java.lang.annotation.Annotation;

public enum  HttpMethod {
    GET,
    DELETE,
    PATCH,
    POST;

    public Class getAnnotation() {
        return null;
    }
}
