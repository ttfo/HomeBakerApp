package com.example.android.homebakerapp.model;

import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;
    private List<Author> authors;
    private String type;

    public Recipe() {}

    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, List<Author> authors, String type) {
        super();
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.authors = authors;
        this.type = type;
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


}

