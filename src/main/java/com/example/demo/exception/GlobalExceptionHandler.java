package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleNotFound(BookNotFoundException ex){
        return ResponseEntity.status(404).body(Map.of("error", ex.getMessage(), "status", 404));

    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handle404(NoResourceFoundException ex) {
        
        Map<String, Object> errorPayload = new LinkedHashMap<>();
        errorPayload.put("timestamp", LocalDateTime.now().toString());
        errorPayload.put("status", HttpStatus.NOT_FOUND.value());
        errorPayload.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        errorPayload.put("message", "The requested API endpoint does not exist.");
        errorPayload.put("path", "/" + ex.getResourcePath());

        return new ResponseEntity<>(errorPayload, HttpStatus.NOT_FOUND);
    }
}

