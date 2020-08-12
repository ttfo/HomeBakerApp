package com.example.android.homebakerapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.homebakerapp.model.Author;

import java.util.List;

@Dao
public interface AuthorDao {

    @Query("SELECT * FROM authors")
    LiveData<List<Author>> loadAllAuthors();

    @Query("SELECT * FROM authors WHERE recipe_id = :recipeId ORDER BY author_recipe_relationship_id")
    List<Author> loadAllAuthorsOfRecipe(int recipeId);

    @Insert
    void insertAuthor(Author author);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAuthor(Author author);

    @Delete
    void deleteAuthor(Author author);

}
