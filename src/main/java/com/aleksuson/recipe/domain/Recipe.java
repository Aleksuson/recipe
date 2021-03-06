package com.aleksuson.recipe.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"categories","ingredient","notes"})
@Entity
@EqualsAndHashCode(exclude = {"categories","ingredient","notes"})
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    @Lob
    private String directions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private Set<Ingredient> ingredient = new HashSet<>();

    @Lob
    private Byte[] image;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Notes notes;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Difficulty difficulty;

    @ManyToMany
    @JoinTable(name = "RECIPE_CATEGORY",
        joinColumns = {@JoinColumn(name = "RECIPE_ID")},
        inverseJoinColumns = {@JoinColumn(name = "CATEGORY_ID")})
    private Set<Category> categories = new HashSet<>();


    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        if(notes != null) {
            this.notes = notes;
            notes.setRecipe(this);
        }
    }

    public Recipe addIngredient(Ingredient ingredien){
        ingredien.setRecipe(this);
        this.ingredient.add(ingredien);
        return this;
    }

}
