package com.example.android.moviesapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesapp.data.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView posterIV;
    private TextView titleTV;
    private TextView releaseTV;
    private TextView voteTV;
    private TextView synopsisTV;
    private Button trailerButton;
    private Button reviewsButton;
    private Button favoritesButton;
    private Movie selectedMovie;

    private static final String LOG_TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        posterIV = findViewById(R.id.poster_iv);
        titleTV = findViewById(R.id.title_tv);
        releaseTV = findViewById(R.id.release_tv);
        voteTV = findViewById(R.id.vote_tv);
        synopsisTV = findViewById(R.id.synopsis_tv);
        trailerButton = findViewById(R.id.button_trailer);
        reviewsButton = findViewById(R.id.button_reviews);
        favoritesButton = findViewById(R.id.button_favorites);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle parcelableExtra = intent.getExtras();
            if (parcelableExtra != null) {
                if (parcelableExtra.containsKey("selectedMovie")) {
                    selectedMovie = intent.getParcelableExtra("selectedMovie");

                    String title = selectedMovie.getTitle();
                    titleTV.setText(title);
                    setTitle(title);

                    String releaseDate = selectedMovie.getReleaseDate();
                    releaseTV.setText(releaseDate);

                    Double vote = selectedMovie.getVote();
                    String voteString = vote.toString();
                    voteTV.setText(voteString);

                    String synopsis = selectedMovie.getOverview();
                    synopsisTV.setText(synopsis);

                    String posterUrl = selectedMovie.getPosterPath();
                    Picasso.with(this).load(posterUrl).into(posterIV);
                } else {
                    Log.i(LOG_TAG, "Parcelable does not contain selected movie info.");
                }
            } else {
                Log.i(LOG_TAG, "Parcelable is null");
            }
        } else {
            Log.i(LOG_TAG, "Intent is null.");
        }

        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ReviewsActivity.class);
                intent.putExtra("selectedMovie", (Parcelable) selectedMovie);
                startActivity(intent);
            }
        });



    }



}

