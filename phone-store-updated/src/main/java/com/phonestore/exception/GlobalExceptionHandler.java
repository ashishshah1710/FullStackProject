package com.phonestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Validation Error");
    error.put("message", ex.getMessage());
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    Map<String, Object> error = new HashMap<>();
    Map<String, String> fieldErrors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((err) -> {
      String fieldName = ((FieldError) err).getField();
      String errorMessage = err.getDefaultMessage();
      fieldErrors.put(fieldName, errorMessage);
    });

    error.put("error", "Validation Failed");
    error.put("message", "Invalid input data");
    error.put("fieldErrors", fieldErrors);
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Constraint Violation");
    error.put("message", ex.getMessage());
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Type Mismatch");
    error.put("message", String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName()));
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Internal Server Error");
    error.put("message", "An unexpected error occurred");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}