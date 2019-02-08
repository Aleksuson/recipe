package com.aleksuson.recipe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.aleksuson.recipe.domain.Recipe;
import com.aleksuson.recipe.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class RecipeServiceImplTest {

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void getRecipeByIdTest(){

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        Recipe returnedRecipe = recipeService.getRecipeById(1L);
        Long id = returnedRecipe.getId();
        Long idExpected =1L;
        assertEquals(idExpected,id);
        verify(recipeRepository, times(1)).findById(1L);
    }

    @Test
    void getRecipesTest() {

        Recipe recipe = new Recipe();
        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);

        when(recipeService.getRecipes()).thenReturn(recipesData);

        Set<Recipe> recipes = recipeService.getRecipes();

        assertEquals(1,recipes.size());
        verify(recipeRepository,times(1)).findAll();
    }
}