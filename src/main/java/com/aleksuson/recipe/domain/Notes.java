package com.aleksuson.recipe.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "notes")
    private Recipe recipe;

    @Lob
    private String recipeNotes;

}
