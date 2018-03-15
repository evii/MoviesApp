package com.example.android.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.moviesapp.data.Movie;
import com.example.android.moviesapp.data.MoviesAdapter;
import com.example.android.moviesapp.data.Reviews;
import com.example.android.moviesapp.data.ReviewsAdapter;
import com.example.android.moviesapp.utilities.ApiClient;
import com.example.android.moviesapp.utilities.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {

    private int movieId;
    private String API_KEY;
    private static final String TAG = "ReviewsActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        API_KEY = this.getResources().getString(R.string.API_key);

        Intent intent = getIntent();
        if (intent != null) {
            Movie selectedMovie = intent.getParcelableExtra("selectedMovie");
            movieId = selectedMovie.getId();
        } else {
            Log.e(TAG, "Intent is null");
        }
        ApiInterface apiInterface =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Reviews.ReviewsResult> call = apiInterface.getMovieReviews(movieId, API_KEY);
        call.enqueue(new Callback<Reviews.ReviewsResult>() {

            @Override
            public void onResponse(Call<Reviews.ReviewsResult> call, Response<Reviews.ReviewsResult> response) {
                List<Reviews> reviews = response.body().getReviewsList();
                TextView noReviewsTv = findViewById(R.id.no_review_tv);

                if (reviews.size() == 0) {
                    noReviewsTv.setVisibility(View.VISIBLE);

                } else {
                    noReviewsTv.setVisibility(View.INVISIBLE);
                    recyclerView = findViewById(R.id.reviews_rec_view);
                    mAdapter = new ReviewsAdapter(reviews);
                    layoutManager = new LinearLayoutManager(getApplicationContext());

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<Reviews.ReviewsResult> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}