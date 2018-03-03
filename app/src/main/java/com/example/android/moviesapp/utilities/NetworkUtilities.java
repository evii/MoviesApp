package com.example.android.moviesapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.moviesapp.Movie;
import com.example.android.moviesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by evi on 24. 2. 2018.
 */

public class NetworkUtilities {
    private final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public final static String sortByPopularity = "popular";
    public final static String sortByVote = "top_rated";
    private final static String API_KEY_PARAM = "api_key";

    public static URL buildUri(Context context, String sortBy) {
        Uri searchUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY_PARAM, context.getResources().getString(R.string.API_key))
                .build();
        URL searchUrl = null;
        try {
            searchUrl = new URL(searchUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return searchUrl;
    }

    public static String getResponseFromUrl(URL url) throws IOException {
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

    public static List<Movie> readJSON(String json) {
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray resultsArray = jsonObject.optJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {

                String title = resultsArray.optJSONObject(i).optString("title");
                String releaseDate = resultsArray.optJSONObject(i).optString("release_date");
                double voteAverage = resultsArray.optJSONObject(i).optDouble("vote_average");
                String overview = resultsArray.optJSONObject(i).optString("overview");
                String posterPath = resultsArray.optJSONObject(i).optString("poster_path");
                Movie movieReadFromJSON = new Movie(title, releaseDate, voteAverage, overview, posterPath);
                movies.add(movieReadFromJSON);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
