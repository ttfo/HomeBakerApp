package com.example.android.homebakerapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.homebakerapp.model.Author;
import com.example.android.homebakerapp.model.Ingredient;
import com.example.android.homebakerapp.model.Measure;
import com.example.android.homebakerapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeDetailsFirstFragment extends Fragment {

    public static final String RECIPE_OBJ_LABEL = "my_recipe";

    private Context mContext;

    // View that holds recipe's notes
    private TextView mNotes;
    // GridLayout that holds list of ingredients with measurements
    private TableLayout mIngredientTable;
    // View that holds recipe's ingredients
    private TextView mIngredientDefault;
    // View that holds recipe's measurements
    private TextView mMeasurementDefault;
    // View that holds recipe's servings details
    private TextView mServings;
    // View that holds recipe's authors details
    private TextView mAuthors;


    // @TODO
    // set click listener on switch
    // hook up switch to settings for fav measurement system

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Recipe clickedRecipeObj = new Recipe();
        mContext = getContext();

        // Check https://stackoverflow.com/questions/17076663/problems-with-settext-in-a-fragment-in-oncreateview
        LayoutInflater lf = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_recipe_details_first, container, false);

        // Layout has been inflated, can set up views

        mNotes = (TextView) view.findViewById(R.id.notes);
        mIngredientTable = (TableLayout) view.findViewById(R.id.ingredients_table);
        mIngredientDefault = (TextView) view.findViewById(R.id.ingredient_tv0);
        mMeasurementDefault = (TextView) view.findViewById(R.id.measurement_tv0);
        mServings = (TextView) view.findViewById(R.id.servings_value);
        mAuthors = (TextView) view.findViewById(R.id.authors_value);

        Bundle bundle = this.getArguments();
        //Log.i("FRAGMENT", "Fragment onCreateView"); <= test point
        if (bundle != null) {
            clickedRecipeObj = (Recipe) bundle.getSerializable(RECIPE_OBJ_LABEL);
            //Log.i("FRAGMENT", "Bundle is not null"); <= test point
            //Log.i("FRAGMENT", "Recipe name: " + clickedRecipeObj.getName()); <= test point
        }


        // RETRIEVE RECIPE DETAILS FROM RECIPE OBJ

        assert clickedRecipeObj != null;
        if (clickedRecipeObj.getNotes().isEmpty()) {
            mNotes.setText(getResources().getString(R.string.empty_notes));
        } else {
            mNotes.setText(clickedRecipeObj.getNotes());
        }

        if (clickedRecipeObj.getServings() == 0) {
            mServings.setText(getResources().getString(R.string.empty_servings));
        } else {
            mServings.setText(String.valueOf(clickedRecipeObj.getServings()));
        }

        List<Author> mAuthorList = clickedRecipeObj.getAuthors();
        StringBuilder sbAuthors = new StringBuilder("");

        if (mAuthorList != null) {
            for (int i = 0; i < mAuthorList.size(); i++) {
                Author author = mAuthorList.get(i);
                if (i == 0) {
                    sbAuthors.append(author.getName());
                } else {
                    sbAuthors.append(", ").append(author.getName());
                }
            }
            if (mAuthorList.isEmpty()) {
                mAuthors.setText(getResources().getString(R.string.empty_authors));
            } else {
                mAuthors.setText(sbAuthors.toString());
            }
        } else {
            mAuthors.setText(getResources().getString(R.string.empty_authors));
        }

        List<Ingredient> mIngredientList = new ArrayList<Ingredient>();
        mIngredientList = clickedRecipeObj.getIngredients();

        Log.i("INGREDIENTS", "mIngredientList.size(): " + String.valueOf(mIngredientList.size()));
        Log.i("INGREDIENTS", "clickedRecipeObj.getIngredients(): " + clickedRecipeObj.getIngredients().toString());

        if (!mIngredientList.isEmpty()) {

            mIngredientDefault.setVisibility(View.GONE);
            mMeasurementDefault.setVisibility(View.GONE);

            for (int i = 0; i < mIngredientList.size(); i++) {

                Ingredient mIngredientObj = mIngredientList.get(i);
                Measure mMeasureObj = mIngredientObj.getMeasure();
                String ingredientName = mIngredientObj.getIngredientName();
                String ingredientMeasure = mMeasureObj.getMeasurementUnit() + " " + mMeasureObj.getMeasurementValue();

                // adding ingredients programmatically to table
                // also check https://stackoverflow.com/questions/43344466/how-to-add-table-rows-to-table-layout-programmatically
                // OR https://stackoverflow.com/questions/18207470/adding-table-rows-dynamically-in-android
                // About setting columns width, check:
                // https://stackoverflow.com/questions/32331368/how-do-androidshrinkcolumns-and-androidstretchcolumns-work
                TableRow row = new TableRow(mContext);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                // Check https://developer.android.com/reference/android/widget/TableRow.LayoutParams

                row.setLayoutParams(lp);
                // set background color of table row, on odd rows
                if (i % 2 != 0){
                    row.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorTableRow));
                 }
                TextView tvIngredient = new TextView(mContext);
                tvIngredient.setText(ingredientName);
                row.addView(tvIngredient);
                TextView tvMeasure = new TextView(mContext);
                tvMeasure.setText(ingredientMeasure);

                tvIngredient.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvIngredient.setMaxWidth(500);
                tvIngredient.setPadding(10,0,10,0);

                tvMeasure.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvMeasure.setMinimumWidth(100);

                row.addView(tvMeasure);

                mIngredientTable.addView(row);
            }
        }

        // Code below is just for testing purposes (would replace button text with the recipe name)
//        mButton = (Button) view.findViewById(R.id.button_first);
//        mButton.setText(clickedRecipeObj.getName());

        // Inflate the layout for this fragment
        return view;
    }

    private void addRowToGridLayout(ViewGroup viewGroup) {
        mContext = getContext();
        TextView ingredientTv = new TextView(mContext);
        // TODO
        viewGroup.addView(ingredientTv);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Default code in template to navigate between fragments
        // As we load fragments dynamically, we can't employ this method
//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(RecipeDetailsFirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }
}