package com.aleksuson.recipe.repositories;

import com.aleksuson.recipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Optional<Recipe> findRecipeById(Long id);
}
