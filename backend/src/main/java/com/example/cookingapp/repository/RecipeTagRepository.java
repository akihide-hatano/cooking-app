package com.example.cookingapp.repository;

import com.example.cookingapp.entity.RecipeTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeTagRepository extends JpaRepository<RecipeTag, Long> {}
