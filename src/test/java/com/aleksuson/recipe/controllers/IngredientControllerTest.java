package com.aleksuson.recipe.controllers;

import com.aleksuson.recipe.commands.IngredientCommand;
import com.aleksuson.recipe.commands.RecipeCommand;
import com.aleksuson.recipe.domain.Ingredient;
import com.aleksuson.recipe.domain.Recipe;
import com.aleksuson.recipe.domain.UnitOfMeasure;
import com.aleksuson.recipe.repositories.RecipeRepository;
import com.aleksuson.recipe.services.IngredientService;
import com.aleksuson.recipe.services.RecipeService;
import com.aleksuson.recipe.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


class IngredientControllerTest {

    @InjectMocks
    IngredientController ingredientController;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

    }

    @Test
    void testListIngredients() throws Exception {
        // given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        lenient().when(recipeService.findCommandById(1L)).thenReturn(recipeCommand);

        //when
        mockMvc.perform(get("/recipe/1/ingredient/"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        //then
        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    void testShowIngredient() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(1L);
        //when
        lenient().when(ingredientService.findByRecipeIdAndIngredientId(1L, 2L)).thenReturn(ingredientCommand);

        //then
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(view().name("recipe/ingredient/show"));

        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyLong(), anyLong());
    }


    @Test
    void testNewIngredientForm() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        //when
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        //then
        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(recipeService, times(1)).findCommandById(anyLong());

    }

    @Test
    void testUpdateIngredientForm() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();

        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        //then
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
    }


    @Test
    void testSaveOrUpdate() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId(3L);
        command.setRecipeId(2L);

        //when
        when(ingredientService.saveIngredientCommand(any())).thenReturn(command);

        //then
        mockMvc.perform(post("/recipe/2/ingredients")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));

    }

    @Test
    void testDeleteIngredient() throws Exception {
        mockMvc.perform(get("/recipe/2/ingredient/3/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredient/"));
    }


}
