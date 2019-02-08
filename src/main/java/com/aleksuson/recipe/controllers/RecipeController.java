package com.aleksuson.recipe.controllers;

import com.aleksuson.recipe.domain.Recipe;
import com.aleksuson.recipe.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recipe")
public class RecipeController {

    RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @RequestMapping("/show/{id}")
    public String getRecipeById(@PathVariable("id") Long id, Model model){
        Recipe recipe = recipeService.getRecipeById(id);
        model.addAttribute("recipe", recipe);

        return "recipe/show";
    }
}
