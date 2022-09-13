package ru.server.exception;

import ru.server.constants.HttpStatus;

public class BadRequestHttpException extends HttpException {
    public BadRequestHttpException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
