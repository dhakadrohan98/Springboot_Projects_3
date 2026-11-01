package com.example.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException
            (ResourceNotFoundException exception) {
        Map<String, String> hmap = new HashMap<>();
        hmap.put("error", "Resource not found");
        hmap.put("message", exception.getMessage());
        return new ResponseEntity<>(hmap, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmailException(DuplicateEmailException exception) {
        Map<String, String> hmap = new HashMap<>();
        hmap.put("message", exception.getMessage());
        // most appropriate status code to use when a resource already exists is 409 Conflict
        return new ResponseEntity<>(hmap, HttpStatus.CONFLICT);
    }
}
