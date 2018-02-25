package com.example.android.moviesapp;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesapp.utilities.NetworkUtilities;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.iw);
        Picasso.with(this).load("https://www.albertina.at/site/assets/files/1456/9_pablo_picasso_-_frau_mit_gruenem_hut.1200x0.jpg").into(imageView);


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

    // Async task for connection to internet and retreiving JSON
    public class getMovieJSONTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String JSONResult = "";
            try {
              JSONResult = NetworkUtilities.getResponseFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return JSONResult;
        }

        @Override
        protected void onPostExecute(String s) {
            TextView textView = findViewById(R.id.tw);
            textView.setText(s);
        }
    }
}
