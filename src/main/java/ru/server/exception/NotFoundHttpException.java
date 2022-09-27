package ru.server.exception;

import ru.server.constants.HttpStatus;

public class NotFoundHttpException extends HttpException {

    public NotFoundHttpException() {
        super(HttpStatus.NOT_FOUND);
    }
}
