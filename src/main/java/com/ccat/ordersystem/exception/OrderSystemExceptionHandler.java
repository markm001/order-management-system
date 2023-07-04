package com.ccat.ordersystem.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderSystemExceptionHandler {
    @ExceptionHandler(value = OrderSystemException.class)
    public ResponseEntity<Object> handleErrors(HttpServletRequest request, InvalidRequestException e) {
        ExceptionInfo info = new ExceptionInfo(e.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(info, e.getHttpStatus());
    }

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ExceptionInfo> handleErrors(HttpServletRequest request, Throwable e) {
        ExceptionInfo info = new ExceptionInfo(
                e.getMessage().isBlank() ? "An error occurred." : e.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(info, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private record ExceptionInfo(
            String message,
            String path
    ) { }
}
