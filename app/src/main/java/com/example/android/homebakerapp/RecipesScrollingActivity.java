package com.example.android.homebakerapp;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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


    private MainRecyclerViewAdapter adapter;
    private Context mContext;

    private ArrayList<Recipe> data = new ArrayList<>(); // var used to store Recipe objects in list retrieved from the cloud

    private String actionSortFlag = ""; // var used to store latest status of sorting of choice

    // Create a ProgressBar variable to store a reference to the ProgressBar
    private ProgressBar mLoadingIndicator;

    // Create a variable to store a reference to the error message TextView
    private TextView mErrorMessageDisplay;

    // Create a variable to store a reference to the RecyclerView where film posters will appear
    private RecyclerView recyclerView;

    // Member variable for the Database
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = RecipesScrollingActivity.this;

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
            }
        });


        // TODO show more columns if on tablet
        // REF. https://stackoverflow.com/questions/29579811/changing-number-of-columns-with-gridlayoutmanager-and-recyclerview/32877124

    }

    public void adapterSetUp(Context context, ArrayList<Recipe> myList) {
        adapter = new MainRecyclerViewAdapter(context, myList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
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

    }


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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (myBookOfRecipes == null) {

                return null;

            } else {

                // Populates UI with recipes data
                for (Recipe recipe : myBookOfRecipes) {
                    data.add(recipe); // now data ArrayList holds list of recipes
                }

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
                    showJsonDataView();

                } else {
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }

    }


    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        recyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON View.
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        // First, hide the currently visible data
        recyclerView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


}