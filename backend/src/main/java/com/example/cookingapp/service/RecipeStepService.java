package com.example.cookingapp.service;

import com.example.cookingapp.entity.Recipe;
import com.example.cookingapp.entity.RecipeStep;
import com.example.cookingapp.repository.RecipeRepository;
import com.example.cookingapp.repository.RecipeStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecipeStepService {

  private final RecipeRepository recipeRepository;
  private final RecipeStepRepository recipeStepRepository;
  private final RecipeService recipeService;

  @Transactional
  public RecipeStep createRecipeStep(Long recipeId, String description, Integer sortOrder) {
    // Recipeが存在するか確認し、存在しない場合は例外をスローする
    Recipe recipe =
        recipeRepository
            .findByIdAndDeletedAtIsNull(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("レシピが存在しません。ID: " + recipeId));
    // Recipeの所有者であるか確認し、所有者でない場合は例外をスローする
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Long userId = Long.parseLong(authentication.getPrincipal().toString());

    if (!recipe.getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("このレシピに手順を追加する権限がありません");
    }

    RecipeStep recipeStep = new RecipeStep();
    recipeStep.setRecipe(recipe);
    recipeStep.setDescription(description);
    recipeStep.setSortOrder(sortOrder);
    return recipeStepRepository.save(recipeStep);
  }

  public RecipeStep getRecipeStep(Long recipeId, Long stepId) {
    // Recipe側のgetRecipeStepメソッドを呼び出して、RecipeStepを取得する
    Recipe recipe = recipeService.getRecipe(recipeId);

    // RecipeStepが存在するか確認し、存在しない場合は例外をスローする
    RecipeStep recipeStep =
        recipeStepRepository
            .findByIdAndDeletedAtIsNull(stepId)
            .orElseThrow(() -> new IllegalArgumentException("レシピ手順が存在しません。ID: " + stepId));

    // そのstepが本当にこのrecipeのものか確認する
    if (!recipeStep.getRecipe().getId().equals(recipe.getId())) {
      throw new IllegalArgumentException("指定された手順はこのレシピに属していません。");
    }
    return recipeStep;
  }

  @Transactional
  public RecipeStep updateRecipeStep(
      Long recipeId, Long stepId, String description, Integer sortOrder) {

    // Recipe側のgetRecipeStepメソッドを呼び出して、RecipeStepを取得する
    RecipeStep recipeStep = getRecipeStep(recipeId, stepId);

    // 自分のレシピかどうか確認する
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = Long.parseLong(authentication.getPrincipal().toString());
    if (!recipeStep.getRecipe().getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("このレシピの手順を更新する権限がありません");
    }

    // recipeStepをsetする
    recipeStep.setDescription(description);
    recipeStep.setSortOrder(sortOrder);
    return recipeStepRepository.save(recipeStep);
  }

  @Transactional
  public void deleteRecipeStep(Long recipeId, Long stepId) {
    // Recipe側のgetRecipeStepメソッドを呼び出して、RecipeStepを取得する
    RecipeStep recipeStep = getRecipeStep(recipeId, stepId);

    // 自分のレシピかどうか確認する
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = Long.parseLong(authentication.getPrincipal().toString());
    if (!recipeStep.getRecipe().getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("このレシピの手順を削除する権限がありません");
    }

    // deletedAtに現在時刻をセットして論理削除する
    recipeStep.setDeletedAt(java.time.LocalDateTime.now());
    recipeStepRepository.save(recipeStep);
  }
}
