package com.aleksuson.recipe.converters;


import com.aleksuson.recipe.commands.CategoryCommand;
import com.aleksuson.recipe.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryCommandToCategoryTest {

    private static final Long ID_VALUE = 1L;
    private static final String DESCRIPTION = "description";
    private CategoryCommandToCategory conveter;

    @BeforeEach
    void setUp() {
        conveter = new CategoryCommandToCategory();
    }

    @Test
    void testNullObject() {
        assertNull(conveter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(conveter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        //given
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(ID_VALUE);
        categoryCommand.setDescription(DESCRIPTION);

        //when
        Category category = conveter.convert(categoryCommand);

        //then
        assertEquals(ID_VALUE, Objects.requireNonNull(category).getId());
        assertEquals(DESCRIPTION, category.getDescription());
    }

}