package com.aleksuson.recipe.converters;

import com.aleksuson.recipe.commands.NotesCommand;
import com.aleksuson.recipe.domain.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NotesCommandToNotesTest {

    private static final Long ID_VALUE = 1L;
    private static final String RECIPE_NOTES = "Notes";
    private NotesCommandToNotes converter;

    @BeforeEach
    void setUp(){
        converter = new NotesCommandToNotes();

    }

    @Test
    void testNullParameter(){
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject(){
        assertNotNull(Objects.requireNonNull(converter.convert(new NotesCommand())));
    }

    @Test
    void convert() {
        //given
        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(ID_VALUE);
        notesCommand.setRecipeNotes(RECIPE_NOTES);

        //when
        Notes notes = converter.convert(notesCommand);

        //then
        assertNotNull(notes);
        assertEquals(ID_VALUE, notes.getId());
        assertEquals(RECIPE_NOTES, notes.getRecipeNotes());
    }

}