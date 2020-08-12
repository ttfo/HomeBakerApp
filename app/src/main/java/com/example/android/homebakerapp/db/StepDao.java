package com.example.android.homebakerapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.homebakerapp.model.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * FROM steps")
    LiveData<List<Step>> loadAllSteps();

    @Query("SELECT * FROM steps WHERE recipe_id = :recipeId ORDER BY local_id")
    List<Step> loadAllStepsOfRecipe(int recipeId);

    @Insert
    void insertStep(Step step);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStep(Step step);

    @Delete
    void deleteStep(Step step);
}
