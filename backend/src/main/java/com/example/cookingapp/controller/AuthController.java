package com.example.cookingapp.controller;

import com.example.cookingapp.dto.RegisterRequest;
import com.example.cookingapp.dto.RegisterResponse;
import com.example.cookingapp.entity.User;
import com.example.cookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {

    User user =
        userService.registerUser(request.getName(), request.getEmail(), request.getPassword());

    RegisterResponse response = new RegisterResponse(user.getId(), user.getName(), user.getEmail());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
