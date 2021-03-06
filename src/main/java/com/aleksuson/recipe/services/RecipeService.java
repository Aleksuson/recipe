package com.aleksuson.recipe.services;

import com.aleksuson.recipe.commands.RecipeCommand;
import com.aleksuson.recipe.domain.Recipe;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface RecipeService {

    Set<Recipe> getRecipes();

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    RecipeCommand findCommandById(Long Id);

    Recipe findById(Long id);

    void deleteById(Long id);

}
