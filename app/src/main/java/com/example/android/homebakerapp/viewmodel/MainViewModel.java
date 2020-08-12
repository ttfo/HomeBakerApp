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

    // Add a tasks member variable for a list of Recipe objects wrapped in a LiveData
    //private LiveData<List<Recipe>> recipes;
    private LiveData<List<Author>> authors;
    private LiveData<List<Measure>> measures;
    private LiveData<List<Step>> steps;

    // @TODO CHECK THIS!!
    // https://stackoverflow.com/questions/47335107/how-to-update-livedata-value
//    public LiveData<PagedList<RecipeListPojo>> liveData;
//    private MediatorLiveData<PagedList<RecipeListPojo>> mediatorLiveData;

    public LiveData<List<Recipe>> recipesLD;
    public LiveData<List<Ingredient>> ingredientsLD;

    private MediatorLiveData<List<Recipe>> recipesMLD;

   // private MediatorLiveData<List<Ingredient>> ingredientsMLD;
//
//    public RecipeListViewModel(@NonNull Application application) {
//        super(application);
//        mediatorLiveData = new MediatorLiveData<>();
//    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        recipesMLD = new MediatorLiveData<>();
    }

//
//    public MediatorLiveData<PagedList<RecipeListPojo>> init(RecipeDao recipeDao, RecipeFrom recipeFrom, String orderBy) {
//        liveData = new LivePagedListBuilder(recipeDao.getAllRecipesList(simpleSQLiteQuery), 6).build();
//
//        mediatorLiveData.addSource(liveData, new Observer<PagedList<RecipeListPojo>>() {
//            @Override
//            public void onChanged(@Nullable PagedList<RecipeListPojo> recipeListPojos) {
//                mediatorLiveData.setValue(recipeListPojos);
//            }
//        });
//        return mediatorLiveData;
//    }

    // Also check: https://developer.android.com/reference/kotlin/androidx/lifecycle/MediatorLiveData
    private MediatorLiveData<List<Recipe>> init(RecipeDao recipeDao, IngredientDao ingredientDao) {

        Log.i("MediatorLiveData", "FIRED");

        recipesLD = recipeDao.loadAllRecipes();
        ingredientsLD = ingredientDao.loadAllIngredients();

        recipesMLD.addSource(ingredientsLD, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                List<Recipe> recipesCurrent = recipesMLD.getValue();
                for (Ingredient ingredient : ingredients) {
                    int mRecipeId = ingredient.getRecipeId();
                    for (Recipe recipe : recipesCurrent) {
                        List<Ingredient>mIngredientList = recipe.getIngredients();
                        if (recipe.getLocalId() == mRecipeId) {
                            mIngredientList.add(ingredient);
                            recipe.setIngredients(mIngredientList);
                        }
                    }
                }
                recipesMLD.setValue(recipesCurrent);
            }
        });
        recipesMLD.addSource(recipesLD, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                List<Recipe> recipesCurrent = recipesMLD.getValue();
                for  (Recipe recipe : recipes) {
                    int recipeId = recipe.getLocalId();
                    for (Recipe recipeCurrent : recipesCurrent) {
                        if (recipeCurrent.getLocalId() == recipeId) {
                            recipeCurrent.setName(recipe.getName() + "_MediatorLiveData");
                        }
                    }
                }
                recipesMLD.setValue(recipesCurrent);
            }
        });
        return recipesMLD;
    }


//    public MainViewModel(@NonNull Application application) {
//        super(application);
//        // In the constructor use the loadAllRecipes of the Dao's to initialize the recipes variables
//        AppDatabase database = AppDatabase.getInstance(this.getApplication());
//        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
//        recipes = database.recipeDao().loadAllRecipes();
////        authors = database.authorDao().loadAllAuthors();
//        ingredients = database.ingredientDao().loadAllIngredients();
//        measures = database.measureDao().loadAllMeasures();
//        steps = database.stepDao().loadAllSteps();
//
////        LiveData authors = database.authorDao().loadAllAuthors();
////        LiveData updatedRecipes = Transformations.switchMap(authors, id ->
////
////                );
////
////        void setUserId(String userId) {
////            this.userIdLiveData.setValue(userId);
////        }
//    }
//
////    public List<Recipe> updatedRecipes() {
////        LiveData<List<Recipe>> mRecipes = Transformations.map(mRecipes
////
////        )
////    }
////
//////    LiveData recipes = ...;
////    LiveData repopulateRecipes = Transformations.map(recipes, new Function() {
////    @Override
////    public Object apply(Object recipes) {
////        for (Recipe recipe : recipes) {
////
////        }
////        return "";
////    }
////});
//
    // Create a getter to retrieve fav recipes
    public MediatorLiveData<List<Recipe>> getFavRecipes() {
        Log.i("LiveData", "getFavRecipes()");
        return recipesMLD;
    }
//
//    // Create a getter to retrieve fav movies
//    public LiveData<List<Author>> getFavAuthors() {
//        return authors;
//    }
//
//    // Create a getter to retrieve fav movies
//    public LiveData<List<Ingredient>> getFavIngredients() {
//        return ingredients;
//    }
//
//    // Create a getter to retrieve fav movies
//    public LiveData<List<Measure>> getFavMeasures() {
//        return measures;
//    }
//
//    // Create a getter to retrieve fav movies
//    public LiveData<List<Step>> getFavSteps() {
//        return steps;
//    }
//
}
