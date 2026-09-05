package com.example.cookingapp.service;

import com.example.cookingapp.dto.LoginRequest;
import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {

  @Test
  void testLogin() {
    // 必要なdataをmockする
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    // テスト対象のloginメソッドを呼び出す
    AuthService authService = new AuthService(userRepository, passwordEncoder);

    // 画面のログイン画面から入力されたemailとpasswordを受け取り、ユーザー認証を行う
    LoginRequest request = new LoginRequest();
    request.setEmail("test@example.com");
    request.setPassword("password123");

    // DBに存在するユーザーをmockする
    User user = mock(User.class);

    // UserのPasswordHashをmockする
    when(user.getPasswordHash()).thenReturn("hashedPassword");

    // emailで検索したらUserが返ってくるようにmockする
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

    // UserのemailとpasswordHashが照合する
    when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

    // loinメソッドを呼び出す
    User result = authService.login(request);

    // DBから取得したUserと、loginメソッドの戻り値が一致することを確認する
    assertEquals(user, result);
  }

  @Test
  void testLoginFailsWhenEmailDoseNotExit() {

    // 必要なdataをmockする
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    // テスト対象のloginメソッドを呼び出す
    AuthService authService = new AuthService(userRepository, passwordEncoder);

    // 画面のログイン画面から入力されたemailとpasswordを受け取り、ユーザー認証を行う
    LoginRequest request = new LoginRequest();
    request.setEmail("nonexistent@example.com");
    request.setPassword("password123");

    // emailで検索したらUserが返ってこないようにmockする
    when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

    // logginすると例外を投げることを確認する
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              authService.login(request);
            });

    assertEquals("メールアドレスまたはパスワードが正しくありません", exception.getMessage());
  }

  // パスワードが一致しない場合のテスト
  @Test
  void testLoginFailsWhenPasswordDoesNotMatch() {

    // 必要なdataをmockする
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    // テスト対象のloginメソッドを呼び出す
    AuthService authService = new AuthService(userRepository, passwordEncoder);

    // 画面のログイン画面から入力されたemailとpasswordを受け取り、ユーザー認証を行う
    LoginRequest request = new LoginRequest();
    request.setEmail("test@example.com");
    request.setPassword("wrongPassword");

    // DBに存在するユーザーをmockする
    User user = mock(User.class);

    // UserのPasswordHashをmockする
    when(user.getPasswordHash()).thenReturn("hashedPassword");

    // emailで検索したらUserが返ってくるようにmockする
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

    // UserのemailとpasswordHashが照合しないようにmockする
    when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

    // loginすると例外を投げることを確認する
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              authService.login(request);
            });

    assertEquals("メールアドレスまたはパスワードが正しくありません", exception.getMessage());

    // 絶対にmatchesで確認したことを証明する
    verify(passwordEncoder).matches("wrongPassword", "hashedPassword");
  }
}
