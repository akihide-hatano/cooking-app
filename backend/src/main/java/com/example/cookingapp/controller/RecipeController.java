package com.example.cookingapp.controller;

import com.example.cookingapp.dto.CreateRecipeRequest;
import com.example.cookingapp.dto.CreateRecipeResponse;
import com.example.cookingapp.dto.GetRecipeResponse;
import com.example.cookingapp.dto.UpdateRecipeRequest;
import com.example.cookingapp.dto.UpdateRecipeResponse;
import com.example.cookingapp.entity.Recipe;
import com.example.cookingapp.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

  private final RecipeService recipeService;

  @PostMapping("/create")
  public ResponseEntity<CreateRecipeResponse> createRecipe(
      @RequestBody @Valid CreateRecipeRequest request) {

    Recipe recipe =
        recipeService.createRecipe(
            request.getName(),
            request.getDescription(),
            request.getCookedDate(),
            request.getVisibility());

    CreateRecipeResponse response =
        new CreateRecipeResponse(
            recipe.getId(),
            recipe.getName(),
            recipe.getDescription(),
            recipe.getCookedDate(),
            recipe.getVisibility());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<GetRecipeResponse> getRecipeById(@PathVariable Long id) {

    Recipe recipe = recipeService.getRecipe(id);
    GetRecipeResponse response =
        new GetRecipeResponse(
            recipe.getId(),
            recipe.getName(),
            recipe.getDescription(),
            recipe.getCookedDate(),
            recipe.getVisibility());
    return ResponseEntity.ok(response);
  }

  @PutMapping({"/{id}"})
  public ResponseEntity<UpdateRecipeResponse> updateRecipe(
      @PathVariable Long id, @RequestBody @Valid UpdateRecipeRequest request) {

    // 次にupdateRecipeを呼び出す
    Recipe recipe = recipeService.updateRecipe(id, request);

    UpdateRecipeResponse response =
        new UpdateRecipeResponse(
            recipe.getId(),
            recipe.getName(),
            recipe.getDescription(),
            recipe.getCookedDate(),
            recipe.getVisibility());

    return ResponseEntity.ok(response);
  }

  // recipeを削除するエンドポイントを作成する
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
    recipeService.deleteRecipe(id);
    return ResponseEntity.noContent().build();
  }
}
