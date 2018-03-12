package com.example.android.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import com.example.android.moviesapp.Settings.SettingsActivity;
import com.example.android.moviesapp.utilities.ApiClient;
import com.example.android.moviesapp.utilities.ApiInterface;
import com.example.android.moviesapp.utilities.GetMovieJSONTask;
import com.example.android.moviesapp.utilities.NetworkUtilities;
import com.example.android.moviesapp.utilities.OnTaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;
import java.util.prefs.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted, SharedPreferences.OnSharedPreferenceChangeListener{

    private MoviesAdapter moviesAdapter;
    private GridView gridView;
    public List<Movie> mMovies;
    private String API_KEY;
    private static final String TAG = "MainActivityRetrofit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridview);
        API_KEY = this.getResources().getString(R.string.API_key);

// setting onItemClickListener on the items in gridview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Movie selectedMovie = mMovies.get(position);
                intent.putExtra("selectedMovie", (Parcelable) selectedMovie);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadSortDisplayPreferences(sharedPreferences);
    }

    private void loadSortDisplayPreferences(SharedPreferences sharedPreferences) {
        String preferenceSelected = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.popularity_label));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if (preferenceSelected.equals(getString(R.string.popularity_label))) {

            ApiInterface apiInterface =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<Movie.MovieResult> call = apiInterface.getPopularMovies(API_KEY);
            call.enqueue(new Callback<Movie.MovieResult>() {

                @Override
                public void onResponse(Call<Movie.MovieResult> call, Response<Movie.MovieResult> response) {
                    List<Movie> movies = response.body().getResults();
                    Movie movie1 = movies.get(0);
                    String path = movie1.getPosterPath();

                    Log.v(TAG, "Number of Popular movies received: " + movies.size());
                    Log.v(TAG, "Posterpath: " + path);

                    mMovies = movies;
                    moviesAdapter = new MoviesAdapter(MainActivity.this, movies);
                    gridView.setAdapter(moviesAdapter);

                }

                @Override
                public void onFailure(Call<Movie.MovieResult> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });


            /*URL url = NetworkUtilities.buildUri(getApplicationContext(), NetworkUtilities.sortByPopularity);
            new GetMovieJSONTask(MainActivity.this).execute(url);*/


        } else if (preferenceSelected.equals(getString(R.string.highest_rated_label))) {

            ApiInterface apiInterface =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<Movie.MovieResult> call = apiInterface.getTopRatedMovies(API_KEY);
            call.enqueue(new Callback<Movie.MovieResult>() {

                @Override
                public void onResponse(Call<Movie.MovieResult> call, Response<Movie.MovieResult> response) {
                    List<Movie> movies = response.body().getResults();
                    Log.v(TAG, "Number of top rated movies received: " + movies.size());
                    mMovies = movies;
                    moviesAdapter = new MoviesAdapter(MainActivity.this, movies);
                    gridView.setAdapter(moviesAdapter);
                }

                @Override
                public void onFailure(Call<Movie.MovieResult> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });




        /*    URL url = NetworkUtilities.buildUri(getApplicationContext(), NetworkUtilities.sortByVote);
            new GetMovieJSONTask(MainActivity.this).execute(url);
        */

        } else if (preferenceSelected.equals(getString(R.string.favorites_label))) {
            return;
        } else {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    // Creating menu for sorting by Popularity or by Vote
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Creating functionality for the menu items - sort by Popularity or by Vote
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if (itemClicked == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(List<Movie> movies) {

        mMovies = movies;
        moviesAdapter = new MoviesAdapter(MainActivity.this, movies);
        gridView.setAdapter(moviesAdapter);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getString(R.string.pref_key))) {
            loadSortDisplayPreferences(sharedPreferences);
        }
        else{
            return;
        }
    }

}
