package ru.server.exception;

import ru.server.constants.HttpStatus;

public class UnsupportedHttpMethodException extends HttpException {
    public UnsupportedHttpMethodException() {
        super(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
