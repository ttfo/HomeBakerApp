package com.example.android.homebakerapp.utils;

import android.util.Log;

import com.example.android.homebakerapp.db.Converters;
import com.example.android.homebakerapp.model.Author;
import com.example.android.homebakerapp.model.Ingredient;
import com.example.android.homebakerapp.model.Measure;
import com.example.android.homebakerapp.model.Recipe;
import com.example.android.homebakerapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static List<Recipe> parseRecipesJson(String json) throws JSONException {

        List<Recipe> recipesBook = new ArrayList<Recipe>();

        JSONArray recipesBookObjJson = new JSONArray(json); // large string with recipes list that needs parsing
                                                            // also ref. https://stackoverflow.com/questions/10164741/get-jsonarray-without-array-name

        for (int i = 0; i < recipesBookObjJson.length(); i++) {

            JSONObject recipeInList = recipesBookObjJson.getJSONObject(i);
            Recipe recipe = new Recipe(); // create a new recipe object for each element in the array list

            recipe.setId(recipeInList.getInt("id"));
            recipe.setName(recipeInList.getString("name"));
            recipe.setServings(recipeInList.getInt("servings"));
            if (!recipeInList.getString("image").isEmpty()) {
                recipe.setImage(recipeInList.getString("image"));
            } else {
                if (recipeInList.getString("type").equals("savoury")) {
                    // TODO get default image from resources
                }
                if (recipeInList.getString("type").equals("sweet")) {
                    // TODO get default image from resources
                }
            }

            recipe.setType(recipeInList.getString("type"));

            // Parse array of ingredients within individual recipe
            JSONArray arrayOfIngredientsJson = recipeInList.getJSONArray("ingredients");
            List<Ingredient> ingredientsInRecipe = new ArrayList<Ingredient>();

            for (int j = 0; j < arrayOfIngredientsJson.length(); j++) {

                JSONObject ingredientInList = arrayOfIngredientsJson.getJSONObject(i);
                Ingredient ingredient = new Ingredient(); // create a new ingredient object for each element in the array list

                ingredient.setIngredientName(ingredientInList.getString("ingredientName"));
                ingredient.setIngredientCategory(Converters.convertStringToIngredientCategoryEnum(ingredientInList.getString("ingredientCategory")));

                // Parse measurement obj. of individual ingredient
                // Also check https://stackoverflow.com/questions/11781075/jsonobject-in-jsonobject
                JSONObject measurementObjJson = ingredientInList.getJSONObject("measure");
                Measure measurementOfIngredient = new Measure();

                measurementOfIngredient.setMeasurementUnit(measurementObjJson.getString("measurementUnit"));
                measurementOfIngredient.setMeasurementValue(measurementObjJson.getDouble("measurementValue"));
                measurementOfIngredient.setMeasurementRefUnit(measurementObjJson.getString("measurementRefUnit"));
                measurementOfIngredient.setMeasurementValueRefUnit(measurementObjJson.getDouble("measurementValueRefUnit"));

                measurementOfIngredient.setMeasurementType(Converters.convertStringToMeasurementTypeEnum(measurementObjJson.getString("measurementType")));
                measurementOfIngredient.setMeasurementLocalSystem(Converters.convertStringToMeasurementLocalSystemEnum(measurementObjJson.getString("measurmentLocalSystem")));

                ingredient.setMeasure(measurementOfIngredient);

            }

            // Parse array of steps within individual recipe
            JSONArray arrayOfStepsJson = recipeInList.getJSONArray("steps");
            List<Step> stepsInRecipe = new ArrayList<Step>();

            for (int k = 0; k < arrayOfStepsJson.length(); k++) {

                JSONObject stepInList = arrayOfStepsJson.getJSONObject(i);
                Step step = new Step(); // create a new step object for each element in the array list

                step.setId(stepInList.getInt("id"));
                step.setShortDescription(stepInList.getString("shortDescription"));
                step.setDescription(stepInList.getString("description"));
                step.setVideoURL(stepInList.getString("videoURL"));
                step.setThumbnailURL(stepInList.getString("thumbnailURL"));

                stepsInRecipe.add(step);

            }

            // Parse array of authors within individual recipe
            JSONArray arrayOfAuthorsJson = recipeInList.getJSONArray("authors");
            List<Author> authorsOfRecipe = new ArrayList<Author>();

            for (int l = 0; l < arrayOfAuthorsJson.length(); l++) {

                JSONObject authorInList = arrayOfAuthorsJson.getJSONObject(i);
                Author author = new Author(); // create a new recipe object for each element in the array list

                author.setId(authorInList.getInt("id"));
                author.setName(authorInList.getString("name"));
                author.setWebsite(authorInList.getString("website"));

                authorsOfRecipe.add(author);

            }

            recipe.setIngredients(ingredientsInRecipe);
            recipe.setSteps(stepsInRecipe);
            recipe.setAuthors(authorsOfRecipe);
            recipesBook.add(recipe);

        }

        return recipesBook;

    }

}