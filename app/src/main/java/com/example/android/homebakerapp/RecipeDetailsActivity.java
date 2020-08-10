package com.example.android.homebakerapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.homebakerapp.model.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

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