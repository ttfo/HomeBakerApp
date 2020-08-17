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

    @Query("SELECT * FROM recipes ORDER BY local_id")
    List<Recipe> loadAllRecipesSync();

    // @TODO NEED TO RETURN A RecipeWithDetails OBJ
    // ALSO CHECK: https://stackoverflow.com/questions/60363574/map-new-pojo-class-with-lesser-fields-to-an-existing-room-table
    // @Query("SELECT SUM(stepCount) as total, AVG(stepCount) as average FROM userFitnessDailyRecords where forDay BETWEEN :startDay AND :endDay ORDER BY forDay ASC")
//    @Query("SELECT r.*, im.* " +
//            "FROM recipes r " +
//            "LEFT JOIN " +
//            "(SELECT i.*, m.* FROM ingredients i " +
//            "LEFT JOIN measures m ON i.local_id = m.ingredient_id ORDER BY i.local_id) AS im " +
//            "ON r.local_id = im.recipe_id ORDER BY r.local_id")
//
//    List<RecipeWithDetails> loadAllRecipesSync(); // Synchronous access for use in widget

    @Insert
    void insertRecipe(Recipe recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    // REF. https://stackoverflow.com/questions/47538857/android-room-delete-with-parameters
    @Query("DELETE FROM recipes WHERE local_id = :id")
    void deleteRecipeById(int id);

    // REF. https://www.youtube.com/watch?time_continue=38&v=K3Ul4pQ7tYw&feature=emb_logo
    @Query("SELECT * FROM recipes WHERE local_id = :id") // colon is used to refer to the param provided
    LiveData<Recipe> loadRecipeById(int id);

    @Query("SELECT * FROM recipes WHERE name = :name") // colon is used to refer to the param provided
    LiveData<Recipe> loadLiveDataRecipeByName(String name);

    @Query("SELECT * FROM recipes WHERE name = :name") // colon is used to refer to the param provided
    Recipe loadRecipeByName(String name);

    @Query("SELECT EXISTS(SELECT * FROM recipes WHERE name = :name)") // colon is used to refer to the param provided
    Boolean isRecipeInFavTable(String name);

}