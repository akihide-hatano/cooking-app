package com.example.cookingapp.service;

import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.UserRepository;
import jakarta.transaction.Transactional;
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

  public User loginUser(String email, String password) {

    // emailでユーザーを検索
    User user =
            userRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("メールアドレスまたはパスワードが正しくありません"));

    // パスワードを検証
    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new IllegalArgumentException("メールアドレスまたはパスワードが正しくありません");
    }

    return user;
  }

  public User getUser(Long id) {

    // IDでユーザーを検索
    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
    return user;
  }

  //userを更新する
  @Transactional
  public User updateUser(Long id, String name, String email, String password) {

    // 更新するユーザーを取得
    User user = getUser(id);

    //emailが変更される場合、既に存在するか確認
    if (!user.getEmail().equals(email) && userRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("Emailが既に存在します");
    }

    // ユーザー情報を更新
    user.updateName(name);
    user.updateEmail(email);

    return user;
  }
}
