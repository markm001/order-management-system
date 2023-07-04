package com.ccat.ordersystem.exception;

public class InvalidIdException extends OrderSystemException {
    public InvalidIdException(String message, Object... args) {
        super("Id-Error: " + message, args);
    }
}
