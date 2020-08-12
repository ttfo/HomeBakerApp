package com.example.android.homebakerapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.homebakerapp.model.Measure;

import java.util.List;

@Dao
public interface MeasureDao {

    @Query("SELECT * FROM measures")
    LiveData<List<Measure>> loadAllMeasures();

    @Query("SELECT * FROM measures WHERE ingredient_id = :ingredientId ORDER BY local_id DESC LIMIT 1")
    Measure loadMeasureOfIngredient(int ingredientId);

    @Insert
    void insertMeasure(Measure measure);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMeasure(Measure measure);

    @Delete
    void deleteMeasure(Measure measure);

}
