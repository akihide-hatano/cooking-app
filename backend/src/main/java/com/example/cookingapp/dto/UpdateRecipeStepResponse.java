package com.example.cookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRecipeStepResponse {

  private Long id;
  private String description;
  private Integer sortOrder;
}
