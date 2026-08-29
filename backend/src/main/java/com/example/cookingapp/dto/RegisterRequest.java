package com.example.cookingapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  @NotBlank private String name;

  @NotBlank @Email private String email;

  @NotBlank
  @Size(min = 8, message = "パスワードは8文字以上である必要があります。")
  private String password;
}
