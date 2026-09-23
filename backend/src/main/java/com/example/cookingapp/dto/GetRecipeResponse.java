package com.example.cookingapp.dto;

import com.example.cookingapp.entity.RecipeVisibility;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetRecipeResponse {

  private Long id;
  private String name;
  private String description;
  private LocalDate cookedDate;
  private RecipeVisibility visibility;
}
