package com.example.android.moviesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView posterIV;
    TextView titleTV;
    TextView releaseTV;
    TextView voteTV;
    TextView synopsisTV;

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

        Movie selectedMovie = getIntent().getParcelableExtra("selectedMovie");

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
    }
}
