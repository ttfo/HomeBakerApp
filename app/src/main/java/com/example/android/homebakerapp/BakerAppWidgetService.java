package com.example.android.homebakerapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.homebakerapp.db.AppDatabase;
import com.example.android.homebakerapp.model.Recipe;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

//import com.example.android.mygarden.provider.PlantContract;
//import com.example.android.mygarden.utils.PlantUtils;
//
//import static com.example.android.mygarden.provider.PlantContract.BASE_CONTENT_URI;
//import static com.example.android.mygarden.provider.PlantContract.PATH_PLANTS;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class BakerAppWidgetService extends IntentService {

    public static final String ACTION_OPEN_RANDOM_RECIPE = "com.example.android.homebakerapp.action.open_random_recipe";
    // TODO (3): Create a new action ACTION_UPDATE_PLANT_WIDGETS to handle updating widget UI and
    // implement handleActionUpdatePlantWidgets to query the plant closest to dying and call
    // updatePlantWidgets to refresh widgets

    // Member variable for the Database
    private AppDatabase mDb;

    public BakerAppWidgetService() {
        super("BakerAppWidgetService");
    }

    /**
     * Starts this service to perform WaterPlants action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionOpenRandomRecipe(Context context) {

        Intent intent = new Intent(context, BakerAppWidgetService.class);
        intent.setAction(ACTION_OPEN_RANDOM_RECIPE);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_OPEN_RANDOM_RECIPE.equals(action)) {
                handleActionOpenRandomRecipe();
            }
        }
    }

    // @TODO CHECK https://stackoverflow.com/questions/54362180/access-room-database-to-update-widget-in-onhandleintent-method-of-intentservice !!!
    /**
     * Handle action WaterPlant in the provided background thread with the provided
     * parameters.
     */
    private void handleActionOpenRandomRecipe() {

        Context context = getApplicationContext();
        // init DB instance
        mDb = AppDatabase.getInstance(context);

        final Recipe[] recipe = {new Recipe()};

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakerAppWidget.class));
        Observable.fromCallable(() -> {
            // fromCallable and subscribeOn will make
            // below code run in a worker thread asynchronously
            return mDb.recipeDao().loadAllRecipesSync(); // retrieving list of recipes from DB
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipeList -> {
                    // Put your mainThread works here.
                    if (recipeList != null) {
                        // Build random recipe here
                        if (recipeList.size() > 0) {

                            Random rand = new Random();
                            int randRecipeindex = rand.nextInt(recipeList.size());
                            Log.i("WIDGET", "recipeList.size(): " + String.valueOf(recipeList.size()));
                            Log.i("WIDGET", "randRecipeindex: " + String.valueOf(randRecipeindex));
                            if (randRecipeindex == 0) {
                                recipe[0] = recipeList.get(randRecipeindex);
                            } else {
                                recipe[0] = recipeList.get(randRecipeindex - 1); // avoiding out of bound exception
                            }
                        } else {
                            Log.i("WIDGET", "RECIPE LIST SIZE IS 0");
                        }
                        BakerAppWidget.updateAppWidget(context, appWidgetManager, appWidgetIds[0], recipe[0]);
                    } else {
                        Log.i("WIDGET", "RECIPE LIST IS NULL");
                    }
                });

    }
}