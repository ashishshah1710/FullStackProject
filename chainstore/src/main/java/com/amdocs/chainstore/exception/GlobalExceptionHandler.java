package com.amdocs.chainstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DuplicateStoreException.class)
  public ResponseEntity<Map<String, Object>> handleDuplicateStoreException(
      DuplicateStoreException ex, WebRequest request) {

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.CONFLICT.value());
    errorResponse.put("error", "Conflict");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("path", request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(StoreNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleStoreNotFoundException(
      StoreNotFoundException ex, WebRequest request) {

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.NOT_FOUND.value());
    errorResponse.put("error", "Not Found");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("path", request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(
      Exception ex, WebRequest request) {

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorResponse.put("error", "Internal Server Error");
    errorResponse.put("message", "An unexpected error occurred");
    errorResponse.put("path", request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}