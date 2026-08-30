package com.example.cookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

  private final Long id;
  private final String name;
  private final String email;
}
