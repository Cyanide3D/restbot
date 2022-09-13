package ru.server.exception;

import ru.server.constants.HttpStatus;

public class UnsupportedMTHttpException extends HttpException {

    public UnsupportedMTHttpException() {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
