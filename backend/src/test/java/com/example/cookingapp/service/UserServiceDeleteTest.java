package com.example.cookingapp.service;

import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceDeleteTest {

  // UserServiceのdeleteUserメソッドが正しく動作するかをテスト
  // deleteUserメソッドは、指定されたIDのユーザーを削除する
  @Test
  public void testDeleteUserSuccessForExistingUser() {

    // UserRepositoryのモックを作成する
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    // テスト対象のUserServiceを作成する
    UserService userService = new UserService(userRepository, passwordEncoder);

    // deleteUserメソッドのテストのdataを作成する
    User user = new User("test", "test@example.com", "password");

    // ID=1のユーザーが存在することを前提にする
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    // deleteUserメソッドのテストを実行する
    userService.deleteUser(1L);

    // 論理削除が正しく行われたかを確認する
    Assertions.assertNotNull(user.getDeletedAt(), "ユーザーが削除されていません");

    // deleteUserメソッドが正しく動作するかを確認する
    Mockito.verify(userRepository).findById(1L);
  }

  /// ユーザーが存在しない場合のテスト
  @Test
  public void testDeleteUserFailsForNonExistingUser() {
    // UserRepositoryのモックを作成する
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    // テスト対象のUserServiceを作成する
    UserService userService = new UserService(userRepository, passwordEncoder);

    // IDが存在しないことにする
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // deleteUserメソッドのテストを実行して例外が投げられることを確認する
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.deleteUser(1L);
        });

    // deleteUserメソッドが正しく動作するかを確認する
    Mockito.verify(userRepository).findById(1L);
  }
}
