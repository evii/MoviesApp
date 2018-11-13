package com.example.android.moviesapp;


import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviesapp.data.Movie;
import com.example.android.moviesapp.data.Reviews;
import com.example.android.moviesapp.data.ReviewsAdapter;
import com.example.android.moviesapp.utilities.ApiClient;
import com.example.android.moviesapp.utilities.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsFragment extends Fragment {

    private Movie selectedMovie;
    private Parcelable mSavedRecyclerLayoutState;
    private static final String BUNDLE_RECYCLER_VIEW = "bundleRecyclerView";
    ApiInterface apiInterface;
    private String API_KEY;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mRecAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String LOG_TAG = "Fragment_Reviews";

    public ReviewsFragment() {
        //constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reviews_, container, false);

        API_KEY = BuildConfig.API_KEY;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Bundle parcelableExtra = intent.getExtras();
            if (parcelableExtra != null) {
                if (parcelableExtra.containsKey("selectedMovie")) {
                    selectedMovie = intent.getParcelableExtra("selectedMovie");

                    int movieId = selectedMovie.getId();


                    if (savedInstanceState != null) {
                        mSavedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_VIEW);
                    }

                    Call<Reviews.ReviewsResult> reviewsCall = apiInterface.getMovieReviews(movieId, API_KEY);
                    reviewsCall.enqueue(new Callback<Reviews.ReviewsResult>() {

                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onResponse(Call<Reviews.ReviewsResult> call, Response<Reviews.ReviewsResult> response) {
                            List<Reviews> reviews = response.body().getReviewsList();
                            TextView noReviewsTv = view.findViewById(R.id.no_review_tv);

                            if (reviews.size() == 0) {
                                noReviewsTv.setVisibility(View.VISIBLE);

                            } else {
                                noReviewsTv.setVisibility(View.INVISIBLE);
                                recyclerView = view.findViewById(R.id.reviews_rec_view);
                                mRecAdapter = new ReviewsAdapter(reviews);
                                layoutManager = new LinearLayoutManager(getContext());

                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mRecAdapter);
                                recyclerView.setNestedScrollingEnabled(false);

                                //restore recycler view position
                                if (mSavedRecyclerLayoutState != null) {
                                    recyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Reviews.ReviewsResult> call, Throwable t) {
                            Log.e(LOG_TAG, t.toString());
                        }
                    });

                } else {
                    Log.i(LOG_TAG, "Parcelable does not contain selected movie info.");
                }
            } else {
                Log.i(LOG_TAG, "Parcelable is null");
            }
        } else {
            Log.i(LOG_TAG, "Intent is null.");
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_VIEW, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_VIEW);
        }
    }
}
