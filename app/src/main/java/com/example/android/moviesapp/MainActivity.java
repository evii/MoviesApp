package com.example.android.moviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.moviesapp.utilities.NetworkUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MoviesAdapter moviesAdapter;
    GridView gridView;
    public List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //first loading of movies
        URL url = NetworkUtilities.buildUri(getApplicationContext(), NetworkUtilities.sortByPopularity);
        new getMovieJSONTask().execute(url);

        gridView = findViewById(R.id.gridview);
// setting onItemClickListener on the items in gridview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Movie selectedMovie = movies.get(position);
                intent.putExtra("selectedMovie", (Parcelable) selectedMovie);
                startActivity(intent);
            }
        });
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
        if (itemClicked == R.id.sort_by_poularity) {
            URL url = NetworkUtilities.buildUri(getApplicationContext(), NetworkUtilities.sortByPopularity);
            new getMovieJSONTask().execute(url);
        }
        if (itemClicked == R.id.sort_by_rating) {
            URL url = NetworkUtilities.buildUri(getApplicationContext(), NetworkUtilities.sortByVote);
            new getMovieJSONTask().execute(url);
        }
        return super.onOptionsItemSelected(item);
    }

    // Async task for connection to internet, retreiving and parsing JSON
public class getMovieJSONTask extends AsyncTask<URL, Void, List<Movie>> {

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

            moviesAdapter = new MoviesAdapter(MainActivity.this, movies);
            gridView.setAdapter(moviesAdapter);
            super.onPostExecute(movies);
        }
    }
}
