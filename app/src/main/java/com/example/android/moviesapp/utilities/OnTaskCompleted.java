package com.example.android.moviesapp.utilities;

import com.example.android.moviesapp.Movie;

import java.util.List;

/**
 * Created by evi on 3. 3. 2018.
 */

public interface OnTaskCompleted {
    void onTaskCompleted(List<Movie> movies);
}