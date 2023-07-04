package com.ccat.ordersystem.exception;

import org.springframework.http.HttpStatus;

import java.util.Formatter;

public class InvalidRequestException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;

    public InvalidRequestException(String message, Object... args) {
        this.message = new Formatter().format(message, args).toString();
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
