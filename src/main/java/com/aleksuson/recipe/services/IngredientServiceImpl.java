package com.aleksuson.recipe.services;

import com.aleksuson.recipe.commands.IngredientCommand;
import com.aleksuson.recipe.converters.IngredientToIngredientCommand;
import com.aleksuson.recipe.domain.Ingredient;
import com.aleksuson.recipe.domain.Recipe;
import com.aleksuson.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if(!recipe.isPresent()) {
            //todo impl error handling
            log.error("recipe id not found. Id: " + recipeId);
        }
        Recipe recipeOne = recipe.get();

        Optional<IngredientCommand> ingredientCommand = recipeOne.getIngredient()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
                .findFirst();

        if(!ingredientCommand.isPresent()){
            //todo impl error handling
            log.error("Ingredient id not found. Id: " +ingredientId);
        }

        return ingredientCommand.get();
    }
}
