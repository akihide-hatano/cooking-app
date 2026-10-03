package com.example.cookingapp.service;

import com.example.cookingapp.dto.UpdateRecipeRequest;
import com.example.cookingapp.entity.Family;
import com.example.cookingapp.entity.FamilyMember;
import com.example.cookingapp.entity.Recipe;
import com.example.cookingapp.entity.RecipeVisibility;
import com.example.cookingapp.entity.User;
import com.example.cookingapp.repository.FamilyMemberRepository;
import com.example.cookingapp.repository.RecipeRepository;
import com.example.cookingapp.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

  // recipeRepositoryを使ってrecipeを取得するメソッドを作成する
  public Recipe getRecipe(Long recipeId) {
    // recipeIdを使ってrecipeを取得する
    Recipe recipe =
        recipeRepository
            .findByIdAndDeletedAtIsNull(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("レシピが見つかりません"));

    // Authenticationからユーザーを取得する
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // userIdを取得する
    String userId = authentication.getPrincipal().toString();

    // Longに変換する
    Long userIdLong = Long.parseLong(userId);

    // RecipeがvisibilityがPRIVATEの場合は、Userがrecipeのuserと同じでない場合は例外を投げる
    RecipeVisibility visibility = recipe.getVisibility();
    if (visibility == RecipeVisibility.PRIVATE && !recipe.getUser().getId().equals(userIdLong)) {
      throw new IllegalArgumentException("このレシピは非公開です");
    }

    // RecipeがvisibilityがFAMILYの場合で、Userがrecipeのfamilyに属していない場合は例外を投げる
    List<FamilyMember> familyMembers = familyMemberRepository.findByUserId(userIdLong);
    boolean belongsToFamily = false;

    for (FamilyMember familyMember : familyMembers) {
      if (familyMember.getFamily().getId().equals(recipe.getFamily().getId())) {
        belongsToFamily = true;
        break;
      }
    }
    if (visibility == RecipeVisibility.FAMILY && !belongsToFamily) {
      throw new IllegalArgumentException("このレシピは家族限定です");
    }
    return recipe;
  }

  public Recipe updateRecipe(Long id, UpdateRecipeRequest request) {
    Recipe recipe =
        recipeRepository
            .findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new IllegalArgumentException("レシピが見つかりません"));

    // Authenticationからユーザーを取得する
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // userIdを取得する
    String userId = authentication.getPrincipal().toString();

    // Longに変換する
    Long userIdLong = Long.parseLong(userId);

    // Userがrecipeのuserと同じでない場合は例外を投げる
    if (!recipe.getUser().getId().equals(userIdLong)) {
      throw new IllegalArgumentException("このレシピを更新する権限がありません");
    }

    recipe.setName(request.getName());
    recipe.setDescription(request.getDescription());
    recipe.setCookedDate(request.getCookedDate());
    recipe.setVisibility(request.getVisibility());

    recipeRepository.save(recipe);

    return recipe;
  }

  // recipeRepositoryを使ってrecipeを削除するメソッドを作成する
  public void deleteRecipe(Long id) {
    Recipe recipe =
        recipeRepository
            .findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new IllegalArgumentException("レシピが見つかりません"));

    // Authenticationからユーザーを取得する
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // userIdを取得する
    String userId = authentication.getPrincipal().toString();

    // Longに変換する
    Long userIdLong = Long.parseLong(userId);

    // Userがrecipeのuserと同じでない場合は例外を投げる
    if (!recipe.getUser().getId().equals(userIdLong)) {
      throw new IllegalArgumentException("このレシピを削除する権限がありません");
    }

    // 論理フラグにて削除する場合は日付を入れる
    recipe.setDeletedAt(LocalDateTime.now());

    recipeRepository.save(recipe);
  }
}
