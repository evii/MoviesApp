package com.example.android.moviesapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.moviesapp.DbData.FavoritesContract;
import com.example.android.moviesapp.DbData.FavoritesDbHelper;
import com.example.android.moviesapp.Settings.SettingsActivity;
import com.example.android.moviesapp.data.Movie;
import com.example.android.moviesapp.data.MoviesAdapter;
import com.example.android.moviesapp.utilities.ApiClient;
import com.example.android.moviesapp.utilities.ApiInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MoviesAdapter moviesAdapter;
    private GridView gridView;
    public List<Movie> mMovies;
    private String API_KEY;
    private static final String TAG = "MainActivity";
    private ApiInterface apiInterface;
    private int mCurrentPosition;
    private static final String STATE_POSITION = "statePosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridview);
        API_KEY = this.getResources().getString(R.string.API_key);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mCurrentPosition = gridView.getLastVisiblePosition();

        savedInstanceState.putInt(STATE_POSITION, mCurrentPosition);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPosition = savedInstanceState.getInt(STATE_POSITION);

    }

    private void loadSortDisplayPreferences(SharedPreferences sharedPreferences) {
        String preferenceSelected = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.popularity_label));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if (preferenceSelected.equals(getString(R.string.popularity_label))) {

            Call<Movie.MovieResult> popularMoviesCall = apiInterface.getPopularMovies(API_KEY);
            popularMoviesCall.enqueue(new Callback<Movie.MovieResult>() {

                @Override
                public void onResponse(Call<Movie.MovieResult> call, Response<Movie.MovieResult> response) {
                    List<Movie> movies = response.body().getResults();
                    mMovies = movies;
                    moviesAdapter = new MoviesAdapter(MainActivity.this, movies);
                    gridView.setAdapter(moviesAdapter);
                    gridView.smoothScrollToPosition(mCurrentPosition);
                }

                @Override
                public void onFailure(Call<Movie.MovieResult> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });

        } else if (preferenceSelected.equals(getString(R.string.highest_rated_label))) {

            Call<Movie.MovieResult> ratedMoviesCall = apiInterface.getTopRatedMovies(API_KEY);
            ratedMoviesCall.enqueue(new Callback<Movie.MovieResult>() {

                @Override
                public void onResponse(Call<Movie.MovieResult> call, Response<Movie.MovieResult> response) {
                    List<Movie> movies = response.body().getResults();
                    mMovies = movies;
                    moviesAdapter = new MoviesAdapter(MainActivity.this, movies);
                    gridView.setAdapter(moviesAdapter);
                    gridView.smoothScrollToPosition(mCurrentPosition);
                }

                @Override
                public void onFailure(Call<Movie.MovieResult> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });

        } else if (preferenceSelected.equals(getString(R.string.favorites_label))) {

            File dbFile = this.getDatabasePath(FavoritesDbHelper.DATABASE_NAME);

            if (dbFile.exists()) {
                Cursor cursor = getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI, null, null, null, null);
                int cursorLength = cursor.getCount();

                if (cursorLength != 0) {
                    cursor.moveToFirst();
                    String posterPath;
                    int movieId;
                    List<Movie> favMovies = new ArrayList<>();

                    for (int i = 1; i <= cursorLength; i++) {
                        posterPath = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_URL));
                        movieId = cursor.getInt(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID));
                        favMovies.add(new Movie(posterPath, movieId));
                        cursor.moveToNext();
                    }
                    cursor.close();
                    mMovies = favMovies;
                    Movie movie1 = favMovies.get(0);
                    String url = movie1.getPosterPath();
                    moviesAdapter = new MoviesAdapter(MainActivity.this, mMovies);
                    gridView.setAdapter(moviesAdapter);
                    gridView.smoothScrollToPosition(mCurrentPosition);
                }
                else{
                    Toast.makeText(this, getResources().getString(R.string.no_favorites_message),
                            Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, getResources().getString(R.string.no_favorites_message),
                        Toast.LENGTH_SHORT).show();
            }
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.pref_key))) {
            loadSortDisplayPreferences(sharedPreferences);
        } else {
            return;
        }
    }

    // refreshing grid after going back from detail activity - ensures correct display of favorites.
    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadSortDisplayPreferences(sharedPreferences);
    }
}
