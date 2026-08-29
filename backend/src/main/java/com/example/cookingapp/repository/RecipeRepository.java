package com.example.cookingapp.repository;

import com.example.cookingapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {}
