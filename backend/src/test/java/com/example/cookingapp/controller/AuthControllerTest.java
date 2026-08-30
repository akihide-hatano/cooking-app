package com.example.cookingapp.controller;

import com.example.cookingapp.config.SecurityConfig;
import com.example.cookingapp.entity.User;
import com.example.cookingapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  @Test
  void registerReturnsCreatedForValidRequest() throws Exception {

    User user = new User("testuser", "testuser@example.com", "hashed-password");

    when(userService.registerUser("testuser", "testuser@example.com", "password123"))
        .thenReturn(user);

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                            {
                                              "name": "testuser",
                                              "email": "testuser@example.com",
                                              "password": "password123"
                                            }
                                            """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value(user.getName()))
        .andExpect(jsonPath("$.email").value(user.getEmail()));
  }

  @Test
  void loginReturnsOkForValidRequest() throws Exception {

    User user = new User("testuser", "testuser@example.com", "hashed-password");

    Mockito.when(userService.loginUser("testuser@example.com", "password123")).thenReturn(user);

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                            {
                                              "email": "testuser@example.com",
                                              "password": "password123"
                                            }
                                            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("testuser"))
        .andExpect(jsonPath("$.email").value("testuser@example.com"))
        .andExpect(jsonPath("$.password").doesNotExist())
        .andExpect(jsonPath("$.passwordHash").doesNotExist());

    Mockito.verify(userService).loginUser("testuser@example.com", "password123");
  }
}
