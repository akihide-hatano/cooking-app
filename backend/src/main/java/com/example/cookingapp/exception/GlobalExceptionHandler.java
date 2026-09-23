package com.example.cookingapp.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
      IllegalArgumentException exception) {

    System.out.println("★ IllegalArgumentException: " + exception.getMessage());

    return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleException(
      MethodArgumentNotValidException exception) {

    String message = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    System.out.println("★ MethodArgumentNotValidException: " + message);
    return ResponseEntity.badRequest().body(Map.of("error", message));
  }
}
