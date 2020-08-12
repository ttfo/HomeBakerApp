package com.example.android.homebakerapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.homebakerapp.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredients")
    LiveData<List<Ingredient>> loadAllIngredients();

    @Query("SELECT * FROM ingredients WHERE recipe_id = :recipeId ORDER BY local_id")
    List<Ingredient> loadAllIngredientsOfRecipe(int recipeId);

    @Insert
    void insertIngredient(Ingredient ingredient);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIngredient(Ingredient ingredient);

    @Delete
    void deleteIngredient(Ingredient ingredient);

    @Query("SELECT * FROM ingredients WHERE ingredient_name = :name AND recipe_id = :recipeId ORDER BY local_id DESC LIMIT 1") // colon is used to refer to the param provided
    Ingredient loadIngredient(String name, int recipeId);

}
