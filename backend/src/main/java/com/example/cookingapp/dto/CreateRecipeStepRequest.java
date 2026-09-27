package com.example.cookingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRecipeStepRequest {
  @NotBlank private String description;
  @NotNull private Integer sortOrder;
}
