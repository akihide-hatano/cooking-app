package com.example.cookingapp.service;

import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Test
  void 新規ユーザー登録が成功すること() {

    // 偽物を作る
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    // テスト対象を作る
    UserService userService = new UserService(userRepository, passwordEncoder);

    // emailはまだ存在しないことにする
    Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

    // passwordをハッシュ化したことにする
    Mockito.when(passwordEncoder.encode("password123")).thenReturn("hashed-password");

    // 実際に登録処理を実行
    userService.registerUser("テスト太郎", "test@example.com", "password123");

    // save()されたことを確認
    Mockito.verify(userRepository).save(Mockito.any(User.class));
  }
}
