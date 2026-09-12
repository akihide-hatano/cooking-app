package com.example.cookingapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateMeRequest {

  @NotBlank private String name;

  @Email @NotBlank private String email;
}
