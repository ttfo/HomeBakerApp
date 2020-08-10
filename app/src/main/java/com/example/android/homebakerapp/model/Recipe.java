package com.example.android.homebakerapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "recipes", // ROOM annotation; setting up table to store user's favourite recipes
        // about indexing, ref. https://developer.android.com/training/data-storage/room/defining-data#column-indexing
        indices = {
            @Index(value = {"name", "local_id"})
        })
public class Recipe implements Serializable {

    // TODO
    // CHECK: https://stackoverflow.com/questions/36879770/how-to-save-foreign-key-entities-using-spring-dao &&
    // https://stackoverflow.com/questions/47511750/how-to-use-foreign-key-in-room-persistence-library

    private int id;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    private int localId;
    private String name;
    @Ignore
    private List<Ingredient> ingredients;
    @Ignore
    private List<Step> steps;
    private int servings;
    private String image;
    @Ignore
    private List<Author> authors;
    private String type;
    @ColumnInfo(name = "fav_flag")
    private boolean isFavourite;
    private String notes;

    // constructor that will be used by ROOM
    public Recipe(Integer id, boolean isFavourite) {
        this.id = id;
        this.isFavourite = isFavourite;
    }

    @Ignore
    public Recipe() {}

    @Ignore
    public Recipe(int id, int localId, String name, List<Ingredient> ingredients, List<Step> steps,
                  int servings, List<Author> authors, String type, boolean isFavourite, String notes) {
        super();
        this.id = id;
        this.localId = localId;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.authors = authors;
        this.type = type;
        this.isFavourite = isFavourite;
        this.notes = notes;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public List<Ingredient> getIngredients() {
        return ingredients;
    }


    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    public List<Step> getSteps() {
        return steps;
    }


    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }


    public int getServings() {
        return servings;
    }


    public void setServings(int servings) {
        this.servings = servings;
    }


    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }


    public List<Author> getAuthors() {
        return authors;
    }


    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

