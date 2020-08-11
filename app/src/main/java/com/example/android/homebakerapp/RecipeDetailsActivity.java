package com.example.android.homebakerapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.homebakerapp.db.AppDatabase;
import com.example.android.homebakerapp.db.AppExecutors;
import com.example.android.homebakerapp.model.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;

// Created from Basic Activity template in Android Studio
// REF. https://developer.android.com/studio/projects/templates#BasicActivity
// Also check: https://stackoverflow.com/questions/60948897/why-android-studio-is-creating-two-fragments

/**
 * An activity representing the details of the selected recipe.
 * Clicking on the edit button leads to a {@link RecipeDetailsSecondFragment} showing
 * editable details. This is only possible if the recipe was added to favourite list,
 * which will copy the recipe into the local DB.
 */
public class RecipeDetailsActivity extends AppCompatActivity {

    // New Recipe object to retrieve recipe details from Intent
    private Recipe clickedRecipeObj = new Recipe();

    // Icon in toolbar that will switch if recipe if of type 'savoury' vs. 'sweet'
    private ImageView toolbarIcon_Iv;
    // Fields to store 'star' buttons in on/off status
    private ImageButton starOn;
    private ImageButton starOff;

    // Member variable for the Database
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Setting up views
        setContentView(R.layout.activity_recipe_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarIcon_Iv = findViewById(R.id.toolbarIcon);
        starOn = (ImageButton) findViewById(R.id.star_on_ib);
        starOff = (ImageButton) findViewById(R.id.star_off_ib);


        // init DB instance
        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent recipeClickedIntent = getIntent();
        if (recipeClickedIntent.hasExtra(getResources().getString(R.string.recipe_object_label))) {
            populateUI(); // populates with recipe obj. data
        }

        // Toggle star button
        starOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                starOn.setVisibility(View.VISIBLE);
                starOff.setVisibility(View.INVISIBLE);
                starOn.bringToFront();

                clickedRecipeObj.setFavourite(true);

                // Adds recipes to fav table in ROOM
                final Recipe recipeInFavTable = clickedRecipeObj;

                // call the diskIO execute method with a new Runnable and implement its run method
                // also ref. https://www.youtube.com/watch?time_continue=203&v=c43ruIIZAMg&feature=emb_logo
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    @NonNull
                    public void run() {
                        // Check if recipe exists in DB before adding it again
                        // Recipe name needs to be unique, recipes with same name are not allowed
                        // @TODO might need to improve this logic at some stage in future
                        // Otherwise SQLiteConstraintException: UNIQUE constraint failed: recipes.id would show
                        // Adds recipe to our local DB of favourite recipes
                        Boolean isRecipeInDB = mDb.recipeDao().isRecipeInFavTable(clickedRecipeObj.getName());
                        if (!isRecipeInDB) {
                            mDb.recipeDao().insertRecipe(recipeInFavTable);
                        } else {
                            Toast.makeText(RecipeDetailsActivity.this,
                                    "Another recipe with the same name is already in your list", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });

                Toast.makeText(RecipeDetailsActivity.this, clickedRecipeObj.getName() + " added to your fav list!", Toast.LENGTH_SHORT).show();
            }
        });

        starOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starOff.setVisibility(View.VISIBLE);
                starOn.setVisibility(View.INVISIBLE);
                starOff.bringToFront();

                clickedRecipeObj.setFavourite(false);

                // Removes recipe from fav table in ROOM
                final Recipe recipeOutFavTable = clickedRecipeObj;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @NonNull
                    @Override
                    public void run() {
                        // Removes recipe from our DB of favourite recipes
                        Boolean isRecipeInDB = mDb.recipeDao().isRecipeInFavTable(clickedRecipeObj.getName());
                        Recipe mRecipeInDB = mDb.recipeDao().loadRecipeByName(clickedRecipeObj.getName());
                        if (isRecipeInDB) {
                            //Log.i("DB", "Removing recipe from DB (BEFORE)");
                            mDb.recipeDao().deleteRecipe(mRecipeInDB);
                            //Log.i("DB", "Removing recipe from DB (AFTER)");
                        }
                        finish();
                    }
                });

                Toast.makeText(RecipeDetailsActivity.this, clickedRecipeObj.getName() + " removed from your fav list!", Toast.LENGTH_SHORT).show();
            }
        });

        // FRAGMENT SETUP
        // Useful resources:
        // https://stackoverflow.com/questions/9931993/passing-an-object-from-an-activity-to-a-fragment
        // https://stackoverflow.com/questions/42266436/passing-objects-between-fragments/42266700
        // GOOD TUTORIALS:
        // https://developer.android.com/training/basics/fragments/fragment-ui
        // https://guides.codepath.com/android/creating-and-using-fragments
        final RecipeDetailsFirstFragment firstFragment = new RecipeDetailsFirstFragment();
        final RecipeDetailsSecondFragment secondFragment = new RecipeDetailsSecondFragment();

        final Bundle mRecipeObjBundle = new Bundle();
        mRecipeObjBundle.putSerializable(RecipeDetailsFirstFragment.RECIPE_OBJ_LABEL, (Serializable) clickedRecipeObj);

        firstFragment.setArguments(mRecipeObjBundle);

        Log.i("Fragment parent", "Fragment onCreate");

        this.getSupportFragmentManager().beginTransaction() // FRAGMENT SETUP, also check https://www.youtube.com/watch?v=NpzC9UhCMik
                .replace(R.id.recipe_detail_container, firstFragment) // load fragment into container view (in content_recipe_details.xml)
                .commit();

        // TODO Replace with second fragment if edit button is clicked


        FloatingActionButton fabP = findViewById(R.id.fab_pencil);

        fabP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickedRecipeObj.isFavourite()) {
                    // on click loads second fragment with editable details (notes, ingredients)
                    // only possible if recipe is stored locally (needs to be in 'fav' list)
                    secondFragment.setArguments(mRecipeObjBundle);
                    getSupportFragmentManager().beginTransaction() // FRAGMENT SETUP, also check https://www.youtube.com/watch?v=NpzC9UhCMik
                            .replace(R.id.recipe_detail_container, secondFragment) // load fragment into container view (in content_recipe_details.xml)
                            .commit();
                } else {
                    Snackbar.make(view, "Add recipe to fav list to unlock editing", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }


    private void populateUI() {
        // REF. https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        clickedRecipeObj = (Recipe) getIntent().getSerializableExtra(getResources().getString(R.string.recipe_object_label));

        // title that will appear in toolbar
        setTitle(clickedRecipeObj.getName() + ": details");
        Log.i("Fragment parent", "Recipe name: " + clickedRecipeObj.getName());

        // setting up logo in toolbar
        if(clickedRecipeObj.getType().equals("savoury")) {
            toolbarIcon_Iv.setImageResource(R.drawable.bread_slice);
        }
        if(clickedRecipeObj.getType().equals("sweet")) {
            toolbarIcon_Iv.setImageResource(R.drawable.pastry_cherry);
        }

        // call the diskIO execute method with a new Runnable and implement its run method
        // also ref. https://www.youtube.com/watch?time_continue=203&v=c43ruIIZAMg&feature=emb_logo
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            @NonNull
            public void run() {
                // Toggles star after checking if recipe is in DB
                Boolean isRecipeInDB = mDb.recipeDao().isRecipeInFavTable(clickedRecipeObj.getName());
                if (isRecipeInDB) {
                    clickedRecipeObj.setFavourite(true);
                    // If film was flagged as favourite by user, show 'star on' button, or hide if not fav
                    starOn.setVisibility(View.VISIBLE);
                    starOff.setVisibility(View.INVISIBLE);
                    Log.i("FAV_STATUS_CHECK", "Is fav!");
                } else {
                    clickedRecipeObj.setFavourite(false);
                    starOff.setVisibility(View.VISIBLE);
                    starOn.setVisibility(View.INVISIBLE);
                    Log.i("FAV_STATUS_CHECK", "Is not fav!");
                }
            }
        });

    }

    // Convenience method
    // Base code from https://abhiandroid.com/ui/fragment#:~:text=In%20Android%2C%20Fragment%20is%20a,user%20interface%20in%20an%20Activity.
    // But deprecated getFragmentManager() replaced with getSupportFragmentManager()
//    private void loadFragment(Context context, Fragment fragment) {
//        // create a FragmentManager
//        FragmentManager fm = getSupportFragmentManager(); // Now deprecated, ref. https://developer.android.com/reference/android/app/Activity.html#getFragmentManager%28%29
//        // create a FragmentTransaction to begin the transaction and replace the Fragment
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        // replace the FrameLayout with new Fragment
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.commit(); // save the change
//    }
    // TODO button Steps => should open 'steps' activity

}