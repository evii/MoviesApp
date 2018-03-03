package com.example.android.moviesapp.utilities;

import android.os.AsyncTask;

import com.example.android.moviesapp.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by evi on 3. 3. 2018.
 */

public class GetMovieJSONTask extends AsyncTask<URL, Void, List<Movie>> {
    List<Movie> movies;
    private OnTaskCompleted taskCompleted;

    public GetMovieJSONTask(OnTaskCompleted activityContext) {
        this.taskCompleted = activityContext;
    }

    @Override
    protected List<Movie> doInBackground(URL... urls) {
        URL url = urls[0];
        String JSONResult = "";
        movies = new ArrayList<>();
        try {
            JSONResult = NetworkUtilities.getResponseFromUrl(url);
            movies = NetworkUtilities.readJSON(JSONResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        taskCompleted.onTaskCompleted(movies);
    }
}
