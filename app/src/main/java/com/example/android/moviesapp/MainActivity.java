package com.example.android.moviesapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.moviesapp.utilities.GetMovieJSONTask;
import com.example.android.moviesapp.utilities.NetworkUtilities;
import com.example.android.moviesapp.utilities.OnTaskCompleted;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private MoviesAdapter moviesAdapter;
    GridView gridView;
    public List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //first loading of movies
        URL url = NetworkUtilities.buildUri(getApplicationContext(), NetworkUtilities.sortByPopularity);
        new GetMovieJSONTask(MainActivity.this).execute(url);

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
            new GetMovieJSONTask(MainActivity.this).execute(url);
        }
        if (itemClicked == R.id.sort_by_rating) {
            URL url = NetworkUtilities.buildUri(getApplicationContext(), NetworkUtilities.sortByVote);
            new GetMovieJSONTask(MainActivity.this).execute(url);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(List<Movie> movies) {
        mMovies = movies;
        moviesAdapter = new MoviesAdapter(MainActivity.this, movies);
        gridView.setAdapter(moviesAdapter);
    }
}
