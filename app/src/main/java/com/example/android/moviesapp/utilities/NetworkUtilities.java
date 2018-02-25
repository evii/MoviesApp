package com.example.android.moviesapp.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.android.moviesapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by evi on 24. 2. 2018.
 */

public class NetworkUtilities {
    private final static String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
    private final static String SORT_PARAM = "sort_by";
    public final static String sortByPopularity = "popularity.desc";
    public final static String sortByVote = "vote_average.desc";
    private final static String API_KEY_PARAM = "api_key";

    public static URL buildUri(Context context, String sortBy) {
        Uri searchUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SORT_PARAM, sortBy)
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

}
