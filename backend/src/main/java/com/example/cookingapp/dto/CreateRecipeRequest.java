package com.example.cookingapp.dto;

import com.example.cookingapp.entity.RecipeVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRecipeRequest {

  @NotBlank private String name;
  private String description;
  @NotNull private LocalDate cookedDate;
  @NotNull private RecipeVisibility visibility;
}
