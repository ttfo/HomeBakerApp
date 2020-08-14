package com.example.android.homebakerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.android.homebakerapp.db.AppDatabase;
import com.example.android.homebakerapp.model.Author;
import com.example.android.homebakerapp.model.Ingredient;
import com.example.android.homebakerapp.model.Recipe;
import com.example.android.homebakerapp.model.Step;
import com.example.android.homebakerapp.utils.JsonUtils;
import com.example.android.homebakerapp.utils.NetworkUtils;
import com.example.android.homebakerapp.viewmodel.MainViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class RecipesScrollingActivity extends AppCompatActivity implements MainRecyclerViewAdapter.ItemClickListener, Serializable {


    private Context mContext;
    private ArrayList<Recipe> data = new ArrayList<>(); // var used to store Recipe objects in list retrieved from the cloud
    private MainRecyclerViewAdapter adapter;

    private String actionSortFlag = ""; // var used to store latest status of sorting of choice

    // Create a ProgressBar variable to store a reference to the ProgressBar
    private ProgressBar mLoadingIndicator;

    // Create a variable to store a reference to the error message TextView
    private TextView mErrorMessageDisplay;

    // Create a variable to store a reference to the RecyclerView where recipe images or placeholders will appear
    private RecyclerView recyclerView;

    // Create a variable to store a reference to CollapsingToolbarLayout
    private CollapsingToolbarLayout toolBarLayout;

    // Create a variable to store a reference to Search widget
    private SearchView searchView;

    // Member variable for the Database
    private AppDatabase mDb;

    // Flag needed to switch between 'fav' icons on menu
    private Boolean flagFavRecipesLoaded = false;

    // Flag needed to hide title when search widget is displayed
    private Boolean flagIsTitleEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = RecipesScrollingActivity.this;
        setContentView(R.layout.activity_scrolling);

        // Get a reference to the ProgressBar using findViewById
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Get a reference to the error TextView using findViewById
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mDb = AppDatabase.getInstance(getApplicationContext());

        // font for title
        Typeface dSFont = Typeface.createFromAsset(mContext.getAssets(), "pacifico_font_asset.ttf");

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //toolBarLayout.setTitle("test");
        // REF. https://stackoverflow.com/questions/31738831/how-to-change-collapsingtoolbarlayout-typeface-and-size
        toolBarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
        toolBarLayout.setExpandedTitleGravity(Gravity.CENTER_VERTICAL);
        toolBarLayout.setCollapsedTitleTypeface(dSFont);
        toolBarLayout.setExpandedTitleTypeface(dSFont);

        searchView = (SearchView) findViewById(R.id.toolbar_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Get matching results of search and return them in adapter
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2 && query.length() < 20) {
                    Log.i("SEARCH", "onQueryTextSubmit");
                    callSearch(query);
                } else {
                    Toast.makeText(getApplicationContext(), mContext.getResources()
                            .getString(R.string.query_not_long_enough), Toast.LENGTH_SHORT).show();
                    Log.i("SEARCH", "QUERY TOO SHORT OR TOO LONG");
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2 && newText.length() < 20) {
                    Log.i("SEARCH", "onQueryTextChange");
                    callSearch(newText);
                }
                return true;
            }

            public void callSearch(String query) {
                Log.i("SEARCH", query);
                List<Recipe> recipesMatch = new ArrayList<Recipe>();
                for (Recipe recipe : data) {
                    Log.i("SEARCH", "For loop: " + recipe.getName());
                    if (recipe.getName().toLowerCase().contains(query.toLowerCase())) {
                        Log.i("SEARCH", "For loop(IF): " + recipe.getName());
                        recipesMatch.add(recipe);
                    }
                }
                adapterSetUp(mContext, (ArrayList<Recipe>) recipesMatch);
            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "@TODO Launch create new recipe activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                adapterSetUp(mContext, data);
//                Log.i("BUTTON_CLICK", data.toString());

            }
        });


        try {
            populateUI();
            Log.i("TAG", "UI populated");
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapterSetUp(mContext, data); // also called from onPostExecute, so that data loads when app is launched

    }


    /**
     * Sub-class the loads data asynchronously from the cloud
     * onPreExecute(), doInBackground() and onPostExecute() methods are defined in here
     */
    public class myBookOfRecipes extends AsyncTask<URL, Void, List<Recipe>> {

        URL recipesBookURL;
        String baseURL = mContext.getResources().getString(R.string.HOMEBAKER_CLOUD_BASE_URL);

        List<Recipe> myBookOfRecipes = new ArrayList<Recipe>();

        public myBookOfRecipes() throws IOException {
            recipesBookURL = NetworkUtils.listOfRecipesURLBuilder(baseURL, mContext.getResources().getString(R.string.HOMEBAKER_RECIPES_ENDPOINT),
                    null, null);
        }

        public ArrayList<Recipe> getBookOfRecipes() {
            return (ArrayList<Recipe>) myBookOfRecipes;
        }

        public URL getBookOfRecipesURL() {
            return recipesBookURL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Recipe> doInBackground(URL... params) {
            URL recipesBookURL = params[0];
            String recipesBookURLAsString = null;

            try {
                recipesBookURLAsString = NetworkUtils.getResponseFromHttpUrl(recipesBookURL);
                Log.i("URL", recipesBookURLAsString);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                myBookOfRecipes = JsonUtils.parseRecipesJson(recipesBookURLAsString);
                Log.i("URL", myBookOfRecipes.toString());
                Log.i("URL", myBookOfRecipes.get(0).getName());
                Log.i("URL", myBookOfRecipes.get(0).getIngredients().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (myBookOfRecipes == null) {

                Log.i("TAG", "myBookOfRecipes == null");
                return null;

            } else {

                // Populates UI with recipes data
                for (Recipe recipe : myBookOfRecipes) {
                    data.add(recipe); // now data ArrayList holds list of recipes
                    Log.i("TAG", recipe.getName());
                }
                Log.i("TAG", data.toString());

            }

            return myBookOfRecipes;
        }

        @Override
        protected void onPostExecute(List<Recipe> myBookOfRecipes) {
            // As soon as the loading is complete, hide the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (myBookOfRecipes != null) {
                if (!myBookOfRecipes.isEmpty()) {
                    // Call showJsonDataView if we have valid, non-null results
                    adapterSetUp(mContext, data);
                    showJsonDataView();

                } else {
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }


    //    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            /*
             * Switching between options on the menu
             */
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this,SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;

            case R.id.action_show_fav_list:
                // Loads list of fav recipes from ROOM DB

                if (flagFavRecipesLoaded) {
                    item.setIcon(getDrawable(R.drawable.fav_list));
                    adapterSetUp(mContext, data); // re-loads all recipes from cloud
                    Toast.makeText(getApplicationContext(), mContext.getResources().getString(R.string.action_hide_fav_toast), Toast.LENGTH_SHORT).show();
                    flagFavRecipesLoaded = false;
                } else {
                    item.setIcon(getDrawable(R.drawable.fav_selected));
                    setupFavRecipesVM(); // loads recipes set as favourites
                    Toast.makeText(getApplicationContext(), mContext.getResources().getString(R.string.action_show_fav_toast), Toast.LENGTH_SHORT).show();
                    flagFavRecipesLoaded = true;
                }

                return true;

            case R.id.action_search:
                if (flagIsTitleEnabled) {
                    searchView.setVisibility(View.VISIBLE);
                    toolBarLayout.setTitle("");
                    flagIsTitleEnabled = false;
                } else {
                    searchView.setVisibility(View.INVISIBLE);
                    toolBarLayout.setTitle(mContext.getResources().getString(R.string.app_name));
                    adapterSetUp(mContext, data); // re-load all recipes when moving away from 'search'
                    flagIsTitleEnabled = true;
                }
                Log.i("flagIsTitleEnabled", "flagIsTitleEnabled: " + flagIsTitleEnabled.toString());
                // Hide title when search widget is displayed
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // method required in MainRecyclerViewAdapter
    @Override
    public void onItemClick(View view, int position) {

        Log.i("TAG", "You have clicked: " + adapter.getItem(position).getName() +
                ", at cell position " + position + " in UI grid.");

        Intent childActivityIntent = new Intent(RecipesScrollingActivity.this, RecipeDetailsActivity.class);

        // REF. https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        childActivityIntent.putExtra(getResources().getString(R.string.recipe_object_label), (Serializable) adapter.getItem(position));

        startActivity(childActivityIntent);

    }


    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     */
    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        recyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * This method will make the error message visible and hide the JSON View.
     */
    private void showErrorMessage() {
        // First, hide the currently visible data
        recyclerView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    public void adapterSetUp(Context context, ArrayList<Recipe> myList) {

        adapter = new MainRecyclerViewAdapter(context, myList);
        adapter.setClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_recipes_list);
        recyclerView.setAdapter(adapter);

        // set up the RecyclerView layout
        // desired value of columns is retrieved from dimens.xml
        int numberOfColumns = getResources().getInteger(R.integer.recipe_scroll_columns);;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

    }


    private void populateUI() throws IOException {

        myBookOfRecipes mBOF = new myBookOfRecipes();
        mBOF.execute(mBOF.getBookOfRecipesURL());

        Log.i("URL", mBOF.getBookOfRecipesURL().toString());

    }


    // loading fav recipes via ViewModel
    private void setupFavRecipesVM() {

        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //viewModel.recipesLD;
        viewModel.getFavRecipes().observe(this, new Observer<List<Recipe>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Recipe> recipes) {

                // This list will help to merge Recipe data with its contained POJO's
                List<Recipe> recipesMerged = new ArrayList<>();

                for (int i = 0; i < recipes.size(); i++) {
                    recipesMerged.add(recipes.get(i));
                    Log.i("setupFavRecipesVM_1", recipesMerged.get(i).getName());
                    String recipeName = recipesMerged.get(i).getName();
                    for (Recipe recipeInData : data) {
                        Log.i("setupFavRecipesVM_2", recipeInData.getName());
                        if (recipeName.equals(recipeInData.getName())) {
                            Log.i("setupFavRecipesVM_3", "MATCH");
                            recipesMerged.get(i).setIngredients(recipeInData.getIngredients());
                            recipesMerged.get(i).setAuthors(recipeInData.getAuthors());
                            recipesMerged.get(i).setSteps(recipeInData.getSteps());
                            break;
                        }
                    }
                }

                RecipesScrollingActivity.this.adapterSetUp(RecipesScrollingActivity.this, new ArrayList<Recipe>(recipesMerged));
            }
        });
    }

}