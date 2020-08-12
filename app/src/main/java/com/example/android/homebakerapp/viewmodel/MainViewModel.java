package com.example.android.homebakerapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.android.homebakerapp.db.AppDatabase;
import com.example.android.homebakerapp.model.Author;
import com.example.android.homebakerapp.model.Ingredient;
import com.example.android.homebakerapp.model.Measure;
import com.example.android.homebakerapp.model.Recipe;
import com.example.android.homebakerapp.model.Step;

import java.util.List;

// This ViewModel builds the list of favourite recipes from the DB
public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    // Add a tasks member variable for a list of Recipe objects wrapped in a LiveData
    private LiveData<List<Recipe>> recipes;
    private LiveData<List<Author>> authors;
    private LiveData<List<Ingredient>> ingredients;
    private LiveData<List<Measure>> measures;
    private LiveData<List<Step>> steps;

    public MainViewModel(@NonNull Application application) {
        super(application);
        // In the constructor use the loadAllRecipes of the Dao's to initialize the recipes variables
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        recipes = database.recipeDao().loadAllRecipes();
        authors = database.authorDao().loadAllAuthors();
        ingredients = database.ingredientDao().loadAllIngredients();
        measures = database.measureDao().loadAllMeasures();
        steps = database.stepDao().loadAllSteps();
    }

//    public List<Recipe> updatedRecipes() {
//        LiveData<List<Recipe>> mRecipes = Transformations.map(mRecipes
//
//        )
//    }
//
////    LiveData recipes = ...;
//    LiveData repopulateRecipes = Transformations.map(recipes, new Function() {
//    @Override
//    public Object apply(Object recipes) {
//        for (Recipe recipe : recipes) {
//
//        }
//        return "";
//    }
//});

    // Create a getter to retrieve fav movies
    public LiveData<List<Recipe>> getFavRecipes() {
        return recipes;
    }

    // Create a getter to retrieve fav movies
    public LiveData<List<Author>> getFavAuthors() {
        return authors;
    }

    // Create a getter to retrieve fav movies
    public LiveData<List<Ingredient>> getFavIngredients() {
        return ingredients;
    }

    // Create a getter to retrieve fav movies
    public LiveData<List<Measure>> getFavMeasures() {
        return measures;
    }

    // Create a getter to retrieve fav movies
    public LiveData<List<Step>> getFavSteps() {
        return steps;
    }

}
