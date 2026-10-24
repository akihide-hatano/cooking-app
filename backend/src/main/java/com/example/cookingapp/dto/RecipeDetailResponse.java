package com.example.cookingapp.dto;

import com.example.cookingapp.entity.RecipeVisibility;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipeDetailResponse {

  private Long id;
  private String name;
  private String description;
  private LocalDate cookedDate;
  private RecipeVisibility visibility;
  private String imageUrl;
  private List<RecipeStepResponse> steps;
}
