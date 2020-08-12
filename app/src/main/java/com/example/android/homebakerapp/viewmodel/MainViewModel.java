package com.example.android.homebakerapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.android.homebakerapp.db.AppDatabase;
import com.example.android.homebakerapp.db.IngredientDao;
import com.example.android.homebakerapp.db.RecipeDao;
import com.example.android.homebakerapp.model.Author;
import com.example.android.homebakerapp.model.Ingredient;
import com.example.android.homebakerapp.model.Measure;
import com.example.android.homebakerapp.model.Recipe;
import com.example.android.homebakerapp.model.Step;

import java.util.ArrayList;
import java.util.List;

// This ViewModel builds the list of favourite recipes from the DB
public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    // Add a variable for a list of Recipe objects wrapped in a LiveData
    private LiveData<List<Recipe>> recipes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        recipes = database.recipeDao().loadAllRecipes();
    }

    // Create a getter to retrieve fav recipes
    public LiveData<List<Recipe>> getFavRecipes() {
        return recipes;
    }

}
