package com.example.cookingapp.controller;


import com.example.cookingapp.entity.User;
import com.example.cookingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;
    @GetMapping("/me")
    public User getMe(Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());
        return  userService.getUser(userId);
    }
}
