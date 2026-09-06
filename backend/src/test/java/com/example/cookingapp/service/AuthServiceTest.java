package com.example.cookingapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.cookingapp.dto.LoginRequest;
import com.example.cookingapp.dto.LoginResponse;
import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceTest {

  @Test
  void testLogin() {
    // 必要なdataをmockする
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    JwtService jwtService = mock(JwtService.class);

    // テスト対象のloginメソッドを呼び出す
    AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);

    // 画面のログイン画面から入力されたemailとpasswordを受け取り、ユーザー認証を行う
    LoginRequest request = new LoginRequest();
    request.setEmail("test@example.com");
    request.setPassword("password123");

    // DBに存在するユーザーをmockする
    User user = mock(User.class);

    // UserのIDをmockする
    when(user.getId()).thenReturn(1L);

    // UserのPasswordHashをmockする
    when(user.getName()).thenReturn("Test User");
    when(user.getEmail()).thenReturn("test@example.com");
    when(user.getPasswordHash()).thenReturn("hashedPassword");

    // emailで検索したらUserが返ってくるようにmockする
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

    // UserのemailとpasswordHashが照合する
    when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

    // JWTトークンをmockする
    when(jwtService.generateToken(1L)).thenReturn("mocked-jwt-token");

    // loinメソッドを呼び出す
    LoginResponse result = authService.login(request);

    // resutのUser情報が正しいことを確認する
    assertEquals(1L, result.getId());
    assertEquals("Test User", result.getName());
    assertEquals("test@example.com", result.getEmail());

    // loginメソッドの戻り値がJWTトークンであることを確認する
    assertEquals("mocked-jwt-token", result.getToken());

    // 絶対にgenerateTokenでJWTトークンを生成したことを証明する
    verify(jwtService).generateToken(1L);
  }

  @Test
  void testLoginFailsWhenEmailDoseNotExit() {

    // 必要なdataをmockする
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    JwtService jwtService = mock(JwtService.class);

    // テスト対象のloginメソッドを呼び出す
    AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);

    // 画面のログイン画面から入力されたemailとpasswordを受け取り、ユーザー認証を行う
    LoginRequest request = new LoginRequest();
    request.setEmail("nonexistent@example.com");
    request.setPassword("password123");

    // emailで検索したらUserが返ってこないようにmockする
    when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

    // loginすると例外を投げることを確認する
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              authService.login(request);
            });

    assertEquals("メールアドレスまたはパスワードが正しくありません", exception.getMessage());

    verify(jwtService, never()).generateToken(anyLong());
  }

  // パスワードが一致しない場合のテスト
  @Test
  void testLoginFailsWhenPasswordDoesNotMatch() {

    // 必要なdataをmockする
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    JwtService jwtService = mock(JwtService.class);

    // テスト対象のloginメソッドを呼び出す
    AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);

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

    verify(jwtService, never()).generateToken(anyLong());
  }
}
