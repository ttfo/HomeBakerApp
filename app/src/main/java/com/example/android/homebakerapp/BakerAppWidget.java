package com.example.android.homebakerapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.android.homebakerapp.model.Recipe;

import java.io.Serializable;

/**
 * Implementation of App Widget functionality.
 */
public class BakerAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe randomRecipe) {

        Log.i("WIDGET", "Recipe name: "+ randomRecipe.getName());
//        Recipe randomRecipe = new Recipe();

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baker_app_widget);
        //views.setTextViewText(R.id.appwidget_iv, widgetText);

        // CHECK https://www.youtube.com/watch?v=qHQGkzw1M8M
        Intent intent = new Intent(context, RecipesScrollingActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(context.getString(R.string.recipe_object_label), (Serializable) randomRecipe);
//        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.appwidget_iv, pendingIntent); // OPEN APP

        // Add the BakerAppWidgetService click handler
        Intent randRecipeIntent = new Intent(context, BakerAppWidgetService.class);
        randRecipeIntent.setAction(BakerAppWidgetService.ACTION_OPEN_RANDOM_RECIPE);
        PendingIntent randRecipePendingIntent = PendingIntent.getService(context, 0, randRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Populate text view with recipe name
        views.setTextViewText(R.id.appwidget_button, "Your random recipe for today: " + randomRecipe.getName());
        views.setOnClickPendingIntent(R.id.appwidget_button, randRecipePendingIntent); // @TODO add button

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

//    static void updateBakerAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetId, Recipe recipe) {
//
//        //
//
//    }

    // called when widget is created and at every update interval as set in xml/baker_app_widget_info.xml
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        BakerAppWidgetService.startActionOpenRandomRecipe(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

