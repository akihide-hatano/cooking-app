package com.example.cookingapp.service;

import com.example.cookingapp.entity.Recipe;
import com.example.cookingapp.entity.RecipeStep;
import java.util.List;

public record RecipeDetailData(Recipe recipe, List<RecipeStep> steps) {}
