package com.example.android.moviesapp.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.android.moviesapp.R;

import java.net.MalformedURLException;
import java.net.URL;

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



}
