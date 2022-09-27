package ru.server.exception;

import ru.server.constants.HttpStatus;

public class InternalServerHttpException extends HttpException {
    public InternalServerHttpException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
