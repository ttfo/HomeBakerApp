package com.example.android.homebakerapp.model;

public class Ingredient implements IngredientType  {

    private String ingredientName;
    private Measure measure;
    private IngredientType.ingredientCategory ingredientCategory;

    public Ingredient() {}

    public Ingredient(String ingredientName, Measure measure,
                      com.example.android.homebakerapp.model.IngredientType.ingredientCategory ingredientCategory) {
        super();
        this.ingredientName = ingredientName;
        this.measure = measure;
        this.ingredientCategory = ingredientCategory;
    }


    // interface method
    @Override
    public void setIngredientCategory(ingredientCategory ingredientCategory) {
        this.ingredientCategory = ingredientCategory;
    }


    public String getIngredientName() {
        return ingredientName;
    }


    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }


    public Measure getMeasure() {
        return measure;
    }


    public void setMeasure(Measure measure) {
        this.measure = measure;
    }


    public IngredientType.ingredientCategory getIngredientCategory() {
        return ingredientCategory;
    }


}
