package com.ccat.ordersystem.exception;

import org.springframework.http.HttpStatus;

import java.util.Formatter;
import java.util.function.Supplier;

public class InvalidIdException implements Supplier<RuntimeException> {
    private final String message;
    private final HttpStatus httpStatus;

    public InvalidIdException(String message, Object... args) {
        this.message = new Formatter().format(message, args).toString();
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public RuntimeException get() {
        return new RuntimeException(message);
    }
}
