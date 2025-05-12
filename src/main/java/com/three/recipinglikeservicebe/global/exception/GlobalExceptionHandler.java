package com.three.recipinglikeservicebe.global.exception;

import com.three.recipinglikeservicebe.global.exception.custom.LikeAlreadyExistsException;
import com.three.recipinglikeservicebe.global.exception.custom.LikeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LikeAlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExists(LikeAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<String> handleNotFound(LikeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
    }
}
