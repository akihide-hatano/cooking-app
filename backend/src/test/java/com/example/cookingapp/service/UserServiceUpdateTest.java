package com.example.cookingapp.service;

import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

// userを更新するテスト

class UserServiceUpdateTest {
  @Test
  void updateUserSucceedsForExistingUser() {
    // 偽物を作る
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    // テスト対象を作る
    UserService userService = new UserService(userRepository, passwordEncoder);

    // 既存のユーザーを作る
    User existingUser = new User("既存ユーザー", "test@example.com", "hashed-password");

    // IDが存在することにする
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

    // 実際に更新処理を実行
    User updateUser = new User("更新ユーザー", "update@example.com", "new-hashed-password");
    User result = userService.updateUser(1L, updateUser.getName(), updateUser.getEmail());

    // 更新が成功したことを確認
    Assertions.assertEquals(updateUser.getName(), result.getName());
    Assertions.assertEquals(updateUser.getEmail(), result.getEmail());
    Mockito.verify(userRepository).findById(1L);
  }

  @Test
  void updateUserSucceedsWhenEmailIsUnused() {
    // 偽物を作る
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    // テスト対象を作る
    UserService userService = new UserService(userRepository, passwordEncoder);

    // 既存のユーザーを作る
    User existingUser = new User("既存ユーザー", "test@example.com", "hashed-password");

    // IDが存在することにする
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

    // emailがまだ存在しないことにする
    Mockito.when(userRepository.findByEmail("update@example.com")).thenReturn(Optional.empty());

    // 実際に更新処理を実行
    User result = userService.updateUser(1L, "更新ユーザー", "update@example.com");

    // 更新が成功したことを確認
    Assertions.assertEquals("更新ユーザー", result.getName());
    Assertions.assertEquals("update@example.com", result.getEmail());
  }

  // userが存在しない場合の更新テスト
  @Test
  void updateUserFailsForNonExistingUser() {
    // 偽物を作る
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    // テスト対象を作る
    UserService userService = new UserService(userRepository, passwordEncoder);

    // IDが存在しないことにする
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // 実際に更新処理を実行して例外が投げられることを確認
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.updateUser(1L, "更新ユーザー", "update@example.com");
        });
  }

  // userが存在するが、emailが既に存在する場合の更新テスト
  @Test
  void updateUserFailsForExistingEmail() {

    // 偽物を作る
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    // テスト対象を作る
    UserService userService = new UserService(userRepository, passwordEncoder);

    // 既存のユーザーを作る
    User existingUser = new User("既存ユーザー", "test@example.com", "hashed-password");

    // 更新するユーザーを作る
    User updateUser = new User("更新ユーザー", "update@example.com", "new-hashed-password");

    // IDが存在することにする
    Mockito.when(userRepository.findById(1L))
        .thenReturn(Optional.of(new User("既存ユーザー", "test@example.com", "hashed-password")));

    // emailが既に存在することにする
    Mockito.when(userRepository.findByEmail("update@example.com"))
        .thenReturn(Optional.of(updateUser));

    // 更新処理を実行して例外が投げられることを確認
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.updateUser(1L, "更新ユーザー", "update@example.com");
        });
  }

  // emailを変更しない場合の更新テスト
  @Test
  void updateUserSucceedsWhenEmailIsUnchanged() {

    // 偽物を作る
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    // テスト対象を作る
    UserService userService = new UserService(userRepository, passwordEncoder);

    // 既存のユーザーを作る
    User existingUser = new User("既存ユーザー", "test@example.com", "hashed-password");

    // 変更のないemailを使って更新するユーザーを作る
    User updateUser = new User("更新ユーザー", "test@example.com", "new-hashed-password");

    // IDが存在することにする
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

    // 実際に更新処理を実行
    User result = userService.updateUser(1L, updateUser.getName(), updateUser.getEmail());

    // 更新が成功したことを確認
    Assertions.assertEquals(updateUser.getName(), result.getName());
    Assertions.assertEquals(updateUser.getEmail(), result.getEmail());

    Mockito.verify(userRepository, Mockito.never()).findByEmail(Mockito.anyString());
  }
}
