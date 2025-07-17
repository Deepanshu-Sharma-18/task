package com.example.task.controllers.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles @Valid validation errors for @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        Map<String, Object> body = new HashMap<>();
        body.put("status", "Validation Error");
        body.put("message", errorMessage);
        body.put("code", 400);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Handles validation errors for @PathVariable or @RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        Map<String, Object> body = new HashMap<>();
        body.put("status", "Validation Error");
        body.put("message", errorMessage);
        body.put("code", 400);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
