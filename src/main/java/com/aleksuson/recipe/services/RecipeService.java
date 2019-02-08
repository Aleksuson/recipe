package com.aleksuson.recipe.services;

import com.aleksuson.recipe.commands.RecipeCommand;
import com.aleksuson.recipe.domain.Recipe;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe getRecipeById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

}
