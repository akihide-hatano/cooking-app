package com.example.cookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {

  private Long id;
  private String name;
  private String email;
}
