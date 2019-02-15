package com.aleksuson.recipe.controllers;


import com.aleksuson.recipe.domain.Recipe;
import com.aleksuson.recipe.services.RecipeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @InjectMocks
    IndexController indexController;

    @Mock
    Model modelMock;

    @Mock
    RecipeService recipeServiceMock;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void testMocMVC() {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        try {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(model().attributeExists("recipeList"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getIndexPage() {

        //given
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());
        Recipe recipe = new Recipe();
        recipe.setId(2L);
        recipes.add(recipe);

        when(recipeServiceMock.getRecipes()).thenReturn(recipes);

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = indexController.getIndexPage(modelMock);

        //then
        assertEquals("index",viewName);
        verify(modelMock, times(1)).addAttribute(eq("recipeList"), argumentCaptor.capture());
        verify(recipeServiceMock,times(1)).getRecipes();
        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(2,setInController.size());
    }
}