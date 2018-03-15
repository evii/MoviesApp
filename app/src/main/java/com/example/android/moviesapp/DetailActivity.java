package com.example.android.moviesapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesapp.data.Movie;
import com.example.android.moviesapp.data.Reviews;
import com.example.android.moviesapp.data.ReviewsAdapter;
import com.example.android.moviesapp.data.Trailer;
import com.example.android.moviesapp.data.TrailerAdapter;
import com.example.android.moviesapp.utilities.ApiClient;
import com.example.android.moviesapp.utilities.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView posterIV;
    private TextView titleTV;
    private TextView releaseTV;
    private TextView voteTV;
    private TextView synopsisTV;
    private Button reviewsButton;
    private Button favoritesButton;
    private Movie selectedMovie;
    private int movieId;
    private String API_KEY;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        reviewsButton = findViewById(R.id.button_reviews);
        favoritesButton = findViewById(R.id.button_favorites);
        API_KEY = this.getResources().getString(R.string.API_key);

        // Movie details:
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

        // Reviews:
        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ReviewsActivity.class);
                intent.putExtra("selectedMovie", (Parcelable) selectedMovie);
                startActivity(intent);
            }
        });


        //Trailers:
        movieId = selectedMovie.getId();
        ApiInterface apiInterface =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Trailer.TrailersResults> call = apiInterface.getMovieTrailer(movieId, API_KEY);
        call.enqueue(new Callback<Trailer.TrailersResults>() {

            @Override
            public void onResponse(Call<Trailer.TrailersResults> call, Response<Trailer.TrailersResults> response) {
                List<Trailer> trailers = response.body().getTrailersResults();
                Log.v(LOG_TAG,String.valueOf(trailers.size()));

                recyclerView = findViewById(R.id.trailers_rec_view);
                mAdapter = new TrailerAdapter(trailers);
                layoutManager = new LinearLayoutManager(getApplicationContext());

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<Trailer.TrailersResults> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });


    }
}

