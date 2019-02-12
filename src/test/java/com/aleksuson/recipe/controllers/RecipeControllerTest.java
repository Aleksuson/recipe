package com.aleksuson.recipe.controllers;

import com.aleksuson.recipe.commands.RecipeCommand;
import com.aleksuson.recipe.domain.Recipe;
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

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
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

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
         mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

    }

    @Test
    void recipeGetTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);


        when(recipeService.getRecipeById(1L)).thenReturn(recipe);

        mockMvc.perform(get("/recipe/{recipeId}/show/", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", hasProperty("id", is(1L))))
                .andExpect(view().name("recipe/show"));

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
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "test"))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe/2/show"));

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