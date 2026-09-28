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
}
