package com.example.cookingapp.service;

import com.example.cookingapp.entity.Family;
import com.example.cookingapp.entity.FamilyMember;
import com.example.cookingapp.entity.FamilyMemberRole;
import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.FamilyMemberRepository;
import com.example.cookingapp.repository.FamilyRepository;
import com.example.cookingapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyService {

  // 必要なリポジトリやサービスをここに注入する
  private final UserRepository userRepository;
  private final FamilyRepository familyRepository;
  private final FamilyMemberRepository familyMemberRepository;

  @Transactional
  public Family createFamily(String name) {
    // Authenticationから認証情報を取得する
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // userIdを取得する
    String userId = authentication.getPrincipal().toString();

    // Longに変換する
    Long userIdLong = Long.parseLong(userId);

    // Userが存在しない場合は例外
    User user =
        userRepository
            .findById(userIdLong)
            .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

    // Familyを作成する
    Family family = new Family();
    family.setName(name);
    Family savedFamily = familyRepository.save(family);

    // FamilyMemberを作成して、UserとFamilyを紐付ける
    FamilyMember familyMember = new FamilyMember();
    familyMember.setUser(user);
    familyMember.setFamily(savedFamily);

    // Familyを作ったユーザーをOwnwerにする
    familyMember.setRole(FamilyMemberRole.OWNER);

    // 詰め込んだFamilyMemberを保存する
    familyMemberRepository.save(familyMember);

    return savedFamily;
  }
}
