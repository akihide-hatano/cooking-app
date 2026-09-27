package com.example.cookingapp.service;

import com.example.cookingapp.entity.Recipe;
import com.example.cookingapp.entity.RecipeStep;
import com.example.cookingapp.repository.RecipeRepository;
import com.example.cookingapp.repository.RecipeStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeStepService {

  private final RecipeRepository recipeRepository;
  private final RecipeStepRepository recipeStepRepository;
  private final RecipeService recipeService;

  public RecipeStep createRecipeStep(Long recipeId, String description, Integer sortOrder) {
    // Recipeが存在するか確認し、存在しない場合は例外をスローする
    Recipe recipe =
        recipeRepository
            .findByIdAndDeletedAtIsNull(recipeId)
            .orElseThrow(() -> new IllegalArgumentException("レシピが存在しません。ID: " + recipeId));

    RecipeStep recipeStep = new RecipeStep();
    recipeStep.setRecipe(recipe);
    recipeStep.setDescription(description);
    recipeStep.setSortOrder(sortOrder);
    return recipeStepRepository.save(recipeStep);
  }
}
