package com.example.cookingapp.controller;

import com.example.cookingapp.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final com.example.cookingapp.service.UserService userService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
    // 登録処理をここに実装

    userService.registerUser(request.getName(), request.getEmail(), request.getPassword());
    return ResponseEntity.ok("User registered successfully");
  }
}
