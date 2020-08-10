package com.example.android.homebakerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.homebakerapp.dummy.DummyContent;
import com.example.android.homebakerapp.model.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

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


        //        @TODO FRAGMENT SETUP
        // Also check https://stackoverflow.com/questions/9931993/passing-an-object-from-an-activity-to-a-fragment
        // https://stackoverflow.com/questions/42266436/passing-objects-between-fragments/42266700
        Bundle mRecipeObj = new Bundle();
        mRecipeObj.putSerializable(RecipeDetailsFirstFragment.RECIPE_OBJ_LABEL, (Serializable) clickedRecipeObj);
        RecipeDetailsFirstFragment fragment = new RecipeDetailsFirstFragment();
        fragment.setArguments(mRecipeObj);
        this.getSupportFragmentManager().beginTransaction() // FRAGMENT SETUP, also check https://www.youtube.com/watch?v=NpzC9UhCMik
                .replace(R.id.recipe_detail_container, fragment) // load fragment into container view (in content_recipe_details.xml)
                .commit();

        Intent recipeClickedIntent = getIntent();
        if (recipeClickedIntent.hasExtra(getResources().getString(R.string.recipe_object_label))) {
            populateUI();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void populateUI() {
        // REF. https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        clickedRecipeObj = (Recipe) getIntent().getSerializableExtra(getResources().getString(R.string.recipe_object_label));

        setTitle(clickedRecipeObj.getName() + ": details");
    }

    // TODO button Steps => should open 'steps' activity

}