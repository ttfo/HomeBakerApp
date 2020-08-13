package com.example.android.homebakerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.android.homebakerapp.model.Author;
import com.example.android.homebakerapp.model.Ingredient;
import com.example.android.homebakerapp.model.Measure;
import com.example.android.homebakerapp.model.Recipe;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeDetailsFirstFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

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
    // View that holds the switch for measurements conversion
    private Switch mSwitch;
    // Boolean to check if the customer has chosen to convert values
    private Boolean isConvOn;
    // String to retrieve desired measurement system
    private String mMeasurementSystemPref;
    // Button that points to 'Steps' activity
    private Button goTosteps;
    // My recipe object
    private Recipe clickedRecipeObj;
    // ScrollView that contains all other elements in fragment
    private ScrollView sV;

    // @TODO
    // set click listener on switch
    // hook up switch to settings for fav measurement system

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        clickedRecipeObj = new Recipe();
        mContext = getContext();

        // Check https://stackoverflow.com/questions/17076663/problems-with-settext-in-a-fragment-in-oncreateview
        LayoutInflater lf = requireActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_recipe_details_first, container, false);

        // Layout has been inflated, can set up views

        mNotes = (TextView) view.findViewById(R.id.notes);
        mIngredientTable = (TableLayout) view.findViewById(R.id.ingredients_table);
        mIngredientDefault = (TextView) view.findViewById(R.id.ingredient_tv0);
        mMeasurementDefault = (TextView) view.findViewById(R.id.measurement_tv0);
        mServings = (TextView) view.findViewById(R.id.servings_value);
        mAuthors = (TextView) view.findViewById(R.id.authors_value);
        mSwitch = (Switch) view.findViewById(R.id.measurement_pref_switch);
        goTosteps = (Button) view.findViewById(R.id.go_to_steps_button);
        sV = (ScrollView) view.findViewById(R.id.recipe_details_scroll);

        Bundle bundle = this.getArguments();
        //Log.i("FRAGMENT", "Fragment onCreateView"); <= test point
        if (bundle != null) {
            clickedRecipeObj = (Recipe) bundle.getSerializable(RECIPE_OBJ_LABEL);
            //Log.i("FRAGMENT", "Bundle is not null"); <= test point
            //Log.i("FRAGMENT", "Recipe name: " + clickedRecipeObj.getName()); <= test point
        }

        // Retrieving user preference for measurement conversion
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//        SharedPreferences convertValuesBoolSettings = mContext.getSharedPreferences(getResources().getString(R.string.pref_convert_bool_key), mContext.MODE_PRIVATE);
//        SharedPreferences convertSystemSettings = mContext.getSharedPreferences(getResources().getString(R.string.pref_choose_system_key), mContext.MODE_PRIVATE);
        // Boolean to check if the customer has chosen to convert values
        isConvOn = sharedPreferences.getBoolean(getString(R.string.pref_convert_bool_key),
                getResources().getBoolean(R.bool.pref_conv_on_or_off));
        Log.i("PREF", isConvOn.toString());
        // String to retrieve desired measurement system
        mMeasurementSystemPref = sharedPreferences.getString(getString(R.string.pref_choose_system_key),"");
        Log.i("PREF", mMeasurementSystemPref);

        //isConvOn = true;
        if (isConvOn) {
            mSwitch.setChecked(true);
        } else {
            mSwitch.setChecked(false);
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
            Log.i("AUTHORS", "AUTHORS NOT NULL");
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
            Log.i("AUTHORS", "AUTHORS NULL");
            mAuthors.setText(getResources().getString(R.string.empty_authors));
        }

        List<Ingredient> mIngredientList = new ArrayList<Ingredient>();
        mIngredientList = clickedRecipeObj.getIngredients();

        if (!mIngredientList.isEmpty()) {

            Log.i("INGREDIENTS", "mIngredientList.size(): " + String.valueOf(mIngredientList.size()));
            Log.i("INGREDIENTS", "clickedRecipeObj.getIngredients(): " + clickedRecipeObj.getIngredients().toString());
            mIngredientDefault.setVisibility(View.GONE);
            mMeasurementDefault.setVisibility(View.GONE);

            for (int i = 0; i < mIngredientList.size(); i++) {

                Ingredient mIngredientObj = mIngredientList.get(i);

                Measure mMeasureObj = mIngredientObj.getMeasure();
                String ingredientMeasure = "";
                DecimalFormat df = new DecimalFormat("#.##"); // Check: https://stackoverflow.com/questions/2538787/how-to-print-a-float-with-2-decimal-places-in-java

                // Check if user has chosen to convert values
                if (isConvOn) {
                    Measure convMeasure = new Measure();
                    Log.i("CONV", "CONV IS ON");
                    if (mMeasurementSystemPref.equals("metric")) {
                        if (mMeasureObj.getMeasurementLocalSystem().name().equals("uscs")) { // User chose to have metric values, but recipe contains USCS
                            Log.i("CONV", "METRIC PREF/ USCS VALUE");
                            convMeasure = convMeasure.switchSystem(mMeasureObj);
                            ingredientMeasure = convMeasure.getMeasurementUnit() + " " + df.format(convMeasure.getMeasurementValue());
                        } else {
                            ingredientMeasure = mMeasureObj.getMeasurementUnit() + " " + mMeasureObj.getMeasurementValue();
                        }
                    } else if (mMeasurementSystemPref.equals("uscs")) {
                        if (mMeasureObj.getMeasurementLocalSystem().name().equals("metric")) { // User chose to have USCS values, but recipe contains metric
                            convMeasure = convMeasure.switchSystem(mMeasureObj);
                            ingredientMeasure = convMeasure.getMeasurementUnit() + " " + df.format(convMeasure.getMeasurementValue());
                        } else {
                            ingredientMeasure = mMeasureObj.getMeasurementUnit() + " " + mMeasureObj.getMeasurementValue();
                        }
                    }
                } else {
                    ingredientMeasure = mMeasureObj.getMeasurementUnit() + " " + mMeasureObj.getMeasurementValue();
                }

                String ingredientName = mIngredientObj.getIngredientName();

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

        // When 'go to instructions' button is clicked, move to steps details
        goTosteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startStepsActivity = new Intent(getActivity(),StepListActivity.class);
                startStepsActivity.putExtra(getResources().getString(R.string.recipe_object_label), clickedRecipeObj);
                startActivity(startStepsActivity);

            }
        });

        // Code below is just for testing purposes (would replace button text with the recipe name)
//        mButton = (Button) view.findViewById(R.id.button_first);
//        mButton.setText(clickedRecipeObj.getName());

        // Inflate the layout for this fragment
        return view;
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getString(R.string.pref_convert_bool_key))) {
            if (mSwitch.isChecked() == true) {
                mSwitch.setChecked(false);
            } else {
                mSwitch.setChecked(true);
            }
        }
    }

    // method that activates button on parent activity to scroll to bottom of fragment
    public void scrollToBottom() {
        Log.i("FRAGMENT", "scrollToBottom called");
        sV.fullScroll(ScrollView.FOCUS_DOWN);
    }

}