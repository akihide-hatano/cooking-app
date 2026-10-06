package com.example.cookingapp.controller;

import com.example.cookingapp.dto.CreateRecipeStepRequest;
import com.example.cookingapp.dto.CreateRecipeStepResponse;
import com.example.cookingapp.dto.RecipeStepResponse;
import com.example.cookingapp.dto.UpdateRecipeStepRequest;
import com.example.cookingapp.entity.RecipeStep;
import com.example.cookingapp.service.RecipeStepService;
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
@RequestMapping("/api/recipes/{recipeId}/steps")
@RequiredArgsConstructor
public class RecipeStepController {

  private final RecipeStepService recipeStepService;

  @PostMapping
  public ResponseEntity<CreateRecipeStepResponse> createRecipeStep(
      @PathVariable Long recipeId, @RequestBody @Valid CreateRecipeStepRequest request) {
    RecipeStep recipeStep =
        recipeStepService.createRecipeStep(
            recipeId, request.getDescription(), request.getSortOrder());

    CreateRecipeStepResponse response =
        new CreateRecipeStepResponse(
            recipeStep.getId(), recipeStep.getDescription(), recipeStep.getSortOrder());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{stepId}")
  public ResponseEntity<RecipeStepResponse> getRecipeStep(
      @PathVariable Long recipeId, @PathVariable Long stepId) {

    RecipeStep recipeStep = recipeStepService.getRecipeStep(recipeId, stepId);

    RecipeStepResponse response =
        new RecipeStepResponse(
            recipeStep.getId(), recipeStep.getDescription(), recipeStep.getSortOrder());

    return ResponseEntity.ok(response);
  }

  @PutMapping("/{stepId}")
  public ResponseEntity<RecipeStepResponse> updateRecipeStep(
      @PathVariable Long recipeId,
      @PathVariable Long stepId,
      @RequestBody @Valid UpdateRecipeStepRequest request) {

    RecipeStep recipeStep =
        recipeStepService.updateRecipeStep(
            recipeId, stepId, request.getDescription(), request.getSortOrder());

    RecipeStepResponse response =
        new RecipeStepResponse(
            recipeStep.getId(), recipeStep.getDescription(), recipeStep.getSortOrder());

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{stepId}")
  public ResponseEntity<Void> deleteRecipeStep(
      @PathVariable Long recipeId, @PathVariable Long stepId) {
    recipeStepService.deleteRecipeStep(recipeId, stepId);
    return ResponseEntity.noContent().build();
  }
}
