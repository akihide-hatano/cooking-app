package com.example.cookingapp.service;

import com.example.cookingapp.entity.Family;
import com.example.cookingapp.entity.FamilyMember;
import com.example.cookingapp.entity.Recipe;
import com.example.cookingapp.entity.RecipeVisibility;
import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.FamilyMemberRepository;
import com.example.cookingapp.repository.RecipeRepository;
import com.example.cookingapp.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

  private final RecipeRepository recipeRepository;
  private final UserRepository userRepository;
  private final FamilyMemberRepository familyMemberRepository;

  // recipeRepositoryを使ってrecipeを作成するメソッドを作成する
  public Recipe createRecipe(
      String name, String description, LocalDate cookedDate, RecipeVisibility visibility) {

    // Authenticationからユーザーを取得する
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // userIdを取得する
    String userId = authentication.getPrincipal().toString();

    // Longに変換する
    Long userIdLong = Long.parseLong(userId);

    // Userがいない場合は例外を投げる
    User user =
        userRepository
            .findById(userIdLong)
            .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

    // Userがfamilyに属していない場合は例外を投げる
    if (!familyMemberRepository.existsByUserId(userIdLong)) {
      throw new IllegalArgumentException("ユーザーは家族に属していません");
    }

    // ListにてUserの家族を取得する
    List<FamilyMember> familyMembers = familyMemberRepository.findByUserId(userIdLong);
    FamilyMember familyMember = familyMembers.get(0);
    Family family = familyMember.getFamily();

    // recipeを作成する
    Recipe recipe = new Recipe();
    recipe.setUser(user);
    recipe.setFamily(family);
    recipe.setName(name);
    recipe.setDescription(description);
    recipe.setCookedDate(cookedDate);
    recipe.setVisibility(visibility);

    Recipe savedRecipe = recipeRepository.save(recipe);
    return savedRecipe;
  }
}
