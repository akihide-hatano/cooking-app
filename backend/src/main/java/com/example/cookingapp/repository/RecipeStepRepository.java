package com.example.cookingapp.repository;

import com.example.cookingapp.entity.RecipeStep;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

  Optional<RecipeStep> findByIdAndDeletedAtIsNull(Long id);

  List<RecipeStep> findByRecipeIdAndDeletedAtIsNullOrderBySortOrderAsc(Long recipeId);
}
