package com.ccat.ordersystem.exception;

public class InvalidDateException extends OrderSystemException {
    public InvalidDateException(String message, Object... args) {
        super("Date-Error: " + message, args);
    }
}
