package com.aleksuson.recipe.services;

import com.aleksuson.recipe.domain.Recipe;
import com.aleksuson.recipe.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface RecipeService {

    Set<Recipe> getRecipes();
}
