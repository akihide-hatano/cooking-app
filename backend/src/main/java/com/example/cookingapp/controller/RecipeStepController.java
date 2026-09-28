package com.example.cookingapp.controller;

import com.example.cookingapp.dto.CreateRecipeStepRequest;
import com.example.cookingapp.dto.CreateRecipeStepResponse;
import com.example.cookingapp.entity.RecipeStep;
import com.example.cookingapp.service.RecipeStepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
}
