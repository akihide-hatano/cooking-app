package com.example.cookingapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class JwtServiceTest {

  private final JwtService jwtService = new JwtService();

  @Test
  void testGenerateToken() {

    String token = jwtService.generateToken(1L);
    assertNotNull(token);
    assertFalse(token.isBlank());
  }

  @Test
  void testGetUserIdFromJwt() {

    String token = jwtService.generateToken(1L);
    String userId = jwtService.extractUserId(token);
    assertEquals("1", userId);
  }
}
