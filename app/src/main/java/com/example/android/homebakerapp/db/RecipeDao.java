package com.example.android.homebakerapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.homebakerapp.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    // About foreign keys in DAO --
    // CHECK: https://stackoverflow.com/questions/36879770/how-to-save-foreign-key-entities-using-spring-dao &&
    // https://stackoverflow.com/questions/47511750/how-to-use-foreign-key-in-room-persistence-library

    @Query("SELECT * FROM recipes ORDER BY local_id")
    LiveData<List<Recipe>> loadAllRecipes();

    @Insert
    void insertRecipe(Recipe recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    // REF. https://www.youtube.com/watch?time_continue=38&v=K3Ul4pQ7tYw&feature=emb_logo
    @Query("SELECT * FROM recipes WHERE local_id = :id") // colon is used to refer to the param provided
    LiveData<Recipe> loadRecipeById(int id);

    @Query("SELECT EXISTS(SELECT * FROM recipes WHERE local_id = :id)")
    Boolean isRecipeInFavTable(int id);

}