package com.example.android.homebakerapp.db;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.homebakerapp.model.Recipe;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    // Add a tasks member variable for a list of Recipe objects wrapped in a LiveData
    private LiveData<List<Recipe>> recipes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        // In the constructor use the loadAllTasks of the taskDao to initialize the tasks variable
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        recipes = database.recipeDao().loadAllRecipes();
    }

    // Create a getter to retrieve fav recipes
    public LiveData<List<Recipe>> getFavRecipes() {
        return recipes;
    }
}
