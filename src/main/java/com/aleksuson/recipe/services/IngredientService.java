package com.aleksuson.recipe.services;

import com.aleksuson.recipe.commands.IngredientCommand;


public interface IngredientService{


    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

}
