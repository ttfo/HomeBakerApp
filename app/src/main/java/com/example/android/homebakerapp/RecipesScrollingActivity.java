package com.example.android.homebakerapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.android.homebakerapp.db.AppDatabase;
import com.example.android.homebakerapp.model.Recipe;
import com.example.android.homebakerapp.utils.JsonUtils;
import com.example.android.homebakerapp.utils.NetworkUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    // Member variable for the Database
    private AppDatabase mDb;


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

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // TODO open favourite list on button click

                adapterSetUp(mContext, data);
                Log.i("BUTTON_CLICK", data.toString());

            }
        });


        try {
            populateUI();
            Log.i("TAG", "UI populated");
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapterSetUp(mContext, data); // also called from onPostExecute, so that data loads when app is launched

        // TODO show more columns if on tablet
        // REF. https://stackoverflow.com/questions/29579811/changing-number-of-columns-with-gridlayoutmanager-and-recyclerview/32877124

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

    }


    private void populateUI() throws IOException {

        myBookOfRecipes mBOF = new myBookOfRecipes();
        mBOF.execute(mBOF.getBookOfRecipesURL());

        Log.i("URL", mBOF.getBookOfRecipesURL().toString());

    }

}