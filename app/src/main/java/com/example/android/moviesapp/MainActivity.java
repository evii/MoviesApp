package com.example.android.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.android.moviesapp.utilities.GetMovieJSONTask;
import com.example.android.moviesapp.utilities.NetworkUtilities;
import com.example.android.moviesapp.utilities.OnTaskCompleted;

import java.net.URL;
import java.util.List;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted, SharedPreferences.OnSharedPreferenceChangeListener{

    private MoviesAdapter moviesAdapter;
    private GridView gridView;
    public List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridview);

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

        Log.v("preferenceSelected", preferenceSelected);
        Log.v("preferencelabel",getString(R.string.popularity_label) );
        Log.v("preferencelabel",getString(R.string.highest_rated_label) );
        Log.v("preferencelabel",getString(R.string.favorites_label) );


        if (preferenceSelected == getString(R.string.popularity_label)) {
            URL url = NetworkUtilities.buildUri(getApplicationContext(), NetworkUtilities.sortByPopularity);
            new GetMovieJSONTask(MainActivity.this).execute(url);
        } else if (preferenceSelected == getString(R.string.highest_rated_label)) {
            URL url = NetworkUtilities.buildUri(getApplicationContext(), NetworkUtilities.sortByVote);
            new GetMovieJSONTask(MainActivity.this).execute(url);
        } else if (preferenceSelected == getString(R.string.favorites_label)) {
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
        Log.v("ontaskcompletedcalled", "ontaskcompletedcalled");
        mMovies = movies;
        moviesAdapter = new MoviesAdapter(MainActivity.this, movies);
        gridView.setAdapter(moviesAdapter);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.v("onSharedPreferenceCheck", "key: "+key);
        Log.v ("onSharedPreferenceCheck", "preference: "+(getString(R.string.popularity_label)));




        if(key.equals(getString(R.string.pref_key))) {
            loadSortDisplayPreferences(sharedPreferences);
        }
        else{
            return;
        }
    }
}
