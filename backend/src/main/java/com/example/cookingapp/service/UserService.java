package com.example.cookingapp.service;

import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  // 定義
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User registerUser(String name, String email, String password) {

    // userが存在するか確認する
    if (userRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("Emailが既に存在します");
    }

    // パスワードをハッシュ化する
    String hashedPassword = passwordEncoder.encode(password);

    // Userを作成
    User user = new User(name, email, hashedPassword);

    userRepository.save(user);
    return user;
  }
}
