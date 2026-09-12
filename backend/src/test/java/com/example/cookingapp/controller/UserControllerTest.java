package com.example.cookingapp.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cookingapp.config.SecurityConfig;
import com.example.cookingapp.entity.User;
import com.example.cookingapp.service.JwtService;
import com.example.cookingapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  @MockitoBean private JwtService jwtService;

  @Test
  @WithMockUser(username = "1")
  void getMeReturnCurrentUser() throws Exception {
    // Implement test logic here
    User user = new User("testuser", "testuser@example.com", "hashed-password");

    when(userService.getUser(1L)).thenReturn(user);

    mockMvc
        .perform(get("/api/users/me"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("testuser"))
        .andExpect(jsonPath("$.email").value("testuser@example.com"))
        .andExpect(jsonPath("$.passwordHash").doesNotExist());
  }

  @Test
  @WithMockUser(username = "1")
  void updateReturnUpdateUser() throws Exception {
    User updateUser = new User("updateduser", "updateduser@example.com", "hashed-password");

    when(userService.updateUser(1L, "updateduser", "updateduser@example.com"))
        .thenReturn(updateUser);

    mockMvc
        .perform(
            put("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                    "name": "updateduser",
                                    "email": "updateduser@example.com"
                                }
                                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("updateduser"))
        .andExpect(jsonPath("$.email").value("updateduser@example.com"))
        .andExpect(jsonPath("$.passwordHash").doesNotExist());
  }
}
