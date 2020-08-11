package com.example.android.homebakerapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.homebakerapp.model.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent recipeClickedIntent = getIntent();
        if (recipeClickedIntent.hasExtra(getResources().getString(R.string.recipe_object_label))) {
            populateUI();
        }

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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // on click loads second fragment with editable details (notes, ingredients)
                // only possible if recipe is stored locally (needs to be in 'fav' list)
                secondFragment.setArguments(mRecipeObjBundle);
                getSupportFragmentManager().beginTransaction() // FRAGMENT SETUP, also check https://www.youtube.com/watch?v=NpzC9UhCMik
                        .replace(R.id.recipe_detail_container, secondFragment) // load fragment into container view (in content_recipe_details.xml)
                        .commit();
            }
        });
    }


    private void populateUI() {
        // REF. https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        clickedRecipeObj = (Recipe) getIntent().getSerializableExtra(getResources().getString(R.string.recipe_object_label));

        setTitle(clickedRecipeObj.getName() + ": details");
        Log.i("Fragment parent", "Recipe name: " + clickedRecipeObj.getName());
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