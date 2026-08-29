package com.example.cookingapp.service;

import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Test
  void registerUserSucceedsForNewUser() {

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

  @Test
  void registerUserFailsForExistingEmail() {
    // 偽物を作る
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    // テスト対象を作る
    UserService userService = new UserService(userRepository, passwordEncoder);
    User existingUser = new User("既存ユーザー", "test@example.com", "hashed-password");

    // emailが既に存在することにする
    Mockito.when(userRepository.findByEmail("test@example.com"))
        .thenReturn(Optional.of(existingUser));

    // 実際に登録処理を実行して例外が投げられることを確認
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.registerUser("テスト太郎", "test@example.com", "password123");
        });
  }
}
