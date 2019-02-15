package com.aleksuson.recipe.services;

import com.aleksuson.recipe.commands.IngredientCommand;
import com.aleksuson.recipe.converters.IngredientCommandToIngredient;
import com.aleksuson.recipe.converters.IngredientToIngredientCommand;
import com.aleksuson.recipe.domain.Ingredient;
import com.aleksuson.recipe.domain.Recipe;

import com.aleksuson.recipe.repositories.RecipeRepository;
import com.aleksuson.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(IngredientCommandToIngredient ingredientCommandToIngredient, IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }


    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if(!recipe.isPresent()) {
            //todo impl error handling
            log.error("recipe id not found. Id: " + recipeId);
        }
        Recipe recipeOne = recipe.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipeOne.getIngredient().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map( ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(!ingredientCommandOptional.isPresent()){
            //todo impl error handling
            log.error("Ingredient id not found: " + ingredientId);
        }

        return ingredientCommandOptional.get();

    }


    @Override
    @Transactional
    public void deleteByRecipeIdAndIngredientId(Long recipeId,Long ingredientId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if(!recipe.isPresent()) {
            //todo impl error handling
            log.error("recipe id not found. Id: " + recipeId);
        }
        Recipe recipeOne = recipe.get();
        Optional<Ingredient> optionalRecipe = recipeOne.getIngredient()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst();

        if(!optionalRecipe.isPresent()){
            //todo impl error handling
            log.error("Ingredient id not found: " + ingredientId);
        }

        optionalRecipe.get().setRecipe(null);

        recipeOne.getIngredient().remove(optionalRecipe.get());

    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (!recipeOptional.isPresent()) {

            //todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredient()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository


                        .findById(command.getUnitOfMeasure().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredientCommandToIngredient.convert(command));
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredeintOptional = savedRecipe.getIngredient()
                    .stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            if (!savedIngredeintOptional.isPresent()) {

                savedIngredeintOptional = savedRecipe.getIngredient().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
                        .findFirst();

            }
            //to do check for fail
            return ingredientToIngredientCommand.convert(savedIngredeintOptional.get());
        }
    }

}

