package com.example.cookingapp.repository;

import com.example.cookingapp.entity.Recipe;
import com.example.cookingapp.entity.RecipeVisibility;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
  List<Recipe> findAllByDeletedAtIsNull();

  Optional<Recipe> findByIdAndDeletedAtIsNull(Long id);

  @Query(
      """
             SELECT r
        FROM Recipe r
        WHERE r.deletedAt IS NULL
          AND (
              r.visibility = :publicVisibility
              OR (
                  r.visibility = :familyVisibility
                  AND r.family.id IN :familyIds
              )
              OR (
                  r.visibility = :privateVisibility
                  AND r.user.id = :userId
              )
          )
      """)
  List<Recipe> findVisibleRecipes(
      @Param("userId") Long userId,
      @Param("familyIds") List<Long> familyIds,
      @Param("publicVisibility") RecipeVisibility publicVisibility,
      @Param("familyVisibility") RecipeVisibility familyVisibility,
      @Param("privateVisibility") RecipeVisibility privateVisibility);
}
