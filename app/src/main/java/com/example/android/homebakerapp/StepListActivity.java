package com.example.android.homebakerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.android.homebakerapp.model.Recipe;
import com.example.android.homebakerapp.model.Step;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    // New Recipe object to retrieve recipe details from Intent
    private Recipe clickedRecipeObj = new Recipe();
    // New Step arraylist that holds steps of recipe
    private List<Step> mRecipeSteps = new ArrayList<Step>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get intent from RecipeDetailsActivity
        Intent recipeClickedIntent = getIntent();
        if (recipeClickedIntent.hasExtra(getResources().getString(R.string.recipe_object_label))) {

            clickedRecipeObj = (Recipe) getIntent().getSerializableExtra(getResources().getString(R.string.recipe_object_label));
            mRecipeSteps = clickedRecipeObj.getSteps();
            Log.i("INTENT", clickedRecipeObj.getName());
            //populateUI(); // populates with recipe obj. data

            for (Step step : mRecipeSteps) {
                Log.i("STEP", "STEP: " + step.getShortDescription() + " " + step.getDescription() + " " +
                        step.getId() + " " + step.getLocalId());
            }

        }

        setContentView(R.layout.activity_step_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(clickedRecipeObj.getName() + ": " + toolbar.getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mRecipeSteps, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final StepListActivity mParentActivity;
        private final List<Step> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step step = (Step) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(view.getContext().getResources().getString(R.string.ARG_ITEM_ID), (Serializable) step);
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction() // FRAGMENT SETUP, also check https://www.youtube.com/watch?v=NpzC9UhCMik
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(view.getContext().getResources().getString(R.string.ARG_ITEM_ID), step);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(StepListActivity parent,
                                      List<Step> steps,
                                      boolean twoPane) {
            mValues = steps;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(mValues.get(position).getId()+1)); // step id's start from 0
            holder.mContentView.setText(mValues.get(position).getShortDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    private void populateUI() {

        // @TODO fix to get intent from RecipeDetailsActivity
        // REF. https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        clickedRecipeObj = (Recipe) getIntent().getSerializableExtra(getResources().getString(R.string.recipe_object_label));
        setTitle(clickedRecipeObj.getName() + ": steps");
    }

}