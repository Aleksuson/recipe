package com.aleksuson.recipe.controllers;

import com.aleksuson.recipe.commands.RecipeCommand;
import com.aleksuson.recipe.domain.Recipe;
import com.aleksuson.recipe.exceptions.NotFoundException;
import com.aleksuson.recipe.repositories.RecipeRepository;
import com.aleksuson.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;



import java.util.Optional;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @InjectMocks
    RecipeController recipeController;

    @Mock
    RecipeService recipeService;


    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
         mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                 .setControllerAdvice(new ControllerExceptionHandler())
                 .build();

    }

    @Test
    void recipeGetTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);


        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(get("/recipe/{recipeId}/show/", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", hasProperty("id", is(1L))))
                .andExpect(view().name("recipe/show"));

    }

    @Test
    void recipeGetNotFoundTest() throws Exception {

        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/{recipeId}/show/", 1L))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void recipeGetNumberFormatExTest() throws Exception {

        mockMvc.perform(get("/recipe/asdfa/show/"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }


    @Test
    void newRecipe() throws Exception {

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe",isA(RecipeCommand.class)))
                .andExpect(view().name("recipe/recipeform"));
    }

    @Test
    void saveOrUpdate() throws Exception {

        RecipeCommand command = new RecipeCommand();
        command.setId(2L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "test")
                .param("directions","some directions"))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe/2/show/"));

    }

    @Test
    void testPostNewRecipeFormValidationFail() throws Exception {

        RecipeCommand command = new RecipeCommand();
        command.setId(2L);

        lenient().when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));

    }


    @Test
    void testGetUpdateView() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testDeleteAction() throws Exception {
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService,times(1)).deleteById(anyLong());
    }



}