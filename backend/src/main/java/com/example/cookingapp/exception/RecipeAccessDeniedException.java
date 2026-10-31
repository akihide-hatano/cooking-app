package com.example.cookingapp.exception;

public class RecipeAccessDeniedException extends RuntimeException {
  public RecipeAccessDeniedException(String message) {
    super(message);
  }
}
