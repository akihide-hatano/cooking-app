package com.example.cookingapp.service;

import com.example.cookingapp.dto.LoginRequest;
import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User login(LoginRequest request) {

    // emailでユーザーを検索し、存在しなければ例外を投げる
    User user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("メールアドレスまたはパスワードが正しくありません"));

    // パスワードを検証し、正しくなければ例外を投げる
    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
      throw new IllegalArgumentException("メールアドレスまたはパスワードが正しくありません");
    }

    // ログイン成功の場合、ユーザー情報を返す
    return user;
  }
}
