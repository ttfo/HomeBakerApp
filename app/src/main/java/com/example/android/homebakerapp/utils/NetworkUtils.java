package com.example.android.homebakerapp.utils;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.homebakerapp.model.Recipe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils extends AppCompatActivity {


    /**
     * This method returns the entire result from the HTTP response.
     * REF. UDACITY Exercise T02.06
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    /**
     * This method build the end-point of our webapp
     * from which we can retrieve list of recipes store in the cloud
     * E.g. https://sbs19016-homebaker-webapp.azurewebsites.net/recipe?id=4
     *
     * @param String baseURL, as defined in strings.xml
     * @param String endPoint, as defined in strings.xml
     * @param String paramKey, as defined in strings.xml
     * @param String paramValue
     * @return url The URL of our endpoint.
     */
    public static URL listOfRecipesURLBuilder(String baseURL, String endPoint, String paramKey, String paramValue) {

        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendPath(endPoint)
                .appendQueryParameter(paramKey, paramValue)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }



}
