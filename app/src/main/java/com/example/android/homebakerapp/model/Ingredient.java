package com.example.android.homebakerapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "ingredients",
        foreignKeys ={
                @ForeignKey(entity = Recipe.class,
                        parentColumns = "local_id",
                        childColumns = "recipe_id",
                        onDelete = CASCADE)
        })
public class Ingredient implements IngredientType  {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    private int localId;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;
    @ColumnInfo(name = "ingredient_name")
    private String ingredientName;
    @Ignore
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


    public int getLocalId() {
        return localId;
    }
}
