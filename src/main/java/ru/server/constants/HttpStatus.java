package ru.server.constants;

public enum HttpStatus {

    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "NOT FOUND");

    private final int statusCode;
    private final String status;

    HttpStatus(int statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }
}
