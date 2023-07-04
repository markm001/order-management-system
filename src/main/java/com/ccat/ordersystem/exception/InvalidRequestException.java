package com.ccat.ordersystem.exception;

public class InvalidRequestException extends OrderSystemException {
    public InvalidRequestException(String message, Object... args) {
        super("Request-Error: " + message, args);
    }
}
