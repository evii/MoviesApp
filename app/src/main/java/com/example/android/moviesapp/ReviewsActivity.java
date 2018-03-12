package com.example.android.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.moviesapp.data.Movie;
import com.example.android.moviesapp.data.MoviesAdapter;
import com.example.android.moviesapp.data.Reviews;
import com.example.android.moviesapp.utilities.ApiClient;
import com.example.android.moviesapp.utilities.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {

    private int movieId;
    private String API_KEY;
    private static final String TAG = "ReviewsActivityRetrofit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        API_KEY = this.getResources().getString(R.string.API_key);

        Intent intent = getIntent();
        Movie selectedMovie = intent.getParcelableExtra("selectedMovie");
        movieId = selectedMovie.getId();

        ApiInterface apiInterface =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Reviews.ReviewsResult> call = apiInterface.getMovieReviews(movieId, API_KEY);
        call.enqueue(new Callback<Reviews.ReviewsResult>() {

            @Override
            public void onResponse(Call<Reviews.ReviewsResult> call, Response<Reviews.ReviewsResult> response) {
                List<Reviews> reviews = response.body().getReviewsList();

                Log.v(TAG, "Number of reviews received: " + reviews.size());

                /*TextView reviewTv1 = findViewById(R.id.review1);
                TextView reviewTv2 = findViewById(R.id.review2);

                Reviews review1 = reviews.get(0);
                String content1 = review1.getContent();
                Reviews review2 = reviews.get(1);
                String content2 = review2.getContent();

                reviewTv1.setText(content1);
                reviewTv2.setText(content2);*/


            }

            @Override
            public void onFailure(Call<Reviews.ReviewsResult> call, Throwable t) {
                Log.e(TAG, t.toString());
            }


        });
    }
}