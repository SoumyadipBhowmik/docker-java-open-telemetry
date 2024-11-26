package com.poc.ot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleThrowable(Throwable throwable) {
        if (throwable instanceof HttpClientErrorException ex) {
            logger.warn("Client error: {}", ex.getMessage());
            return ResponseEntity.status(ex.getStatusCode()).body("Client error: " + ex.getMessage());
        } else if (throwable instanceof ResourceAccessException ex) {
            logger.warn("Resource access error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is unavailable: " + ex.getMessage());
        } else {
            logger.error("Unexpected error: {}", throwable.getMessage(), throwable);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + throwable.getMessage());
        }
    }
}
