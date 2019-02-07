package com.aleksuson.recipe.domain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CategoryTest {

   Category category;


   @BeforeEach
   public void setUp(){
       category = new Category();
   }


    @Test
    void getId() {
       Long idValue = 4L;
       category.setId(idValue);
       assertEquals(idValue,category.getId());
    }
    @Test
    void getDescription() {
        String catDescription = "Test description";
        category.setDescription(catDescription);
        assertEquals(catDescription,category.getDescription());
    }

    @Test
    void getRecipes() {

    }
}