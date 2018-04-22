package com.example.android.moviesapp;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.DbData.FavoritesContract;
import com.example.android.moviesapp.data.Movie;
import com.example.android.moviesapp.utilities.ApiClient;
import com.example.android.moviesapp.utilities.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment {

    private ImageView posterIV;
    private TextView titleTV;
    private TextView releaseTV;
    private TextView voteTV;
    private TextView synopsisTV;
    private Button addFavoritesButton;
    private Button removeFavoritesButton;
    private String API_KEY;
    private ApiInterface apiInterface;
    private Movie selectedMovie;
    private String movieTitle;
    private int movieId;

    private static final String LOG_TAG = "Fragment_Details";

    public DetailsFragment() {
        //constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        posterIV = view.findViewById(R.id.poster_iv);
        titleTV = view.findViewById(R.id.title_tv);
        releaseTV = view.findViewById(R.id.release_tv);
        voteTV = view.findViewById(R.id.vote_tv);
        synopsisTV = view.findViewById(R.id.synopsis_tv);
        addFavoritesButton = view.findViewById(R.id.button_add_favorites);
        removeFavoritesButton = view.findViewById(R.id.button_remove_favorites);
        API_KEY = this.getResources().getString(R.string.API_key);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Bundle parcelableExtra = intent.getExtras();
            if (parcelableExtra != null) {
                if (parcelableExtra.containsKey("selectedMovie")) {
                    selectedMovie = intent.getParcelableExtra("selectedMovie");

                    int movieId = selectedMovie.getId();

                    Call<Movie> movieDetailCall = apiInterface.getMovieDetail(movieId, API_KEY);
                    movieDetailCall.enqueue(new Callback<Movie>() {

                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            movieTitle = response.body().getTitle();
                            titleTV.setText(movieTitle);
                            getActivity().setTitle(movieTitle);

                            String releaseDate = response.body().getReleaseDate();
                            releaseTV.setText(releaseDate);

                            Double vote = response.body().getVote();
                            String voteString = vote.toString();
                            voteTV.setText(voteString);

                            String synopsis = response.body().getOverview();
                            synopsisTV.setText(synopsis);
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Log.e(LOG_TAG, t.toString());
                        }
                    });

                    String posterUrl = selectedMovie.getPosterPath();
                    Picasso.with(getActivity()).load(posterUrl).into(posterIV);
                } else {
                    Log.i(LOG_TAG, "Parcelable does not contain selected movie info.");
                }
            } else {
                Log.i(LOG_TAG, "Parcelable is null");
            }
        } else {
            Log.i(LOG_TAG, "Intent is null.");
        }

        // Favorites:
        addFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                String posterUrl = selectedMovie.getPosterPath();
                movieId = selectedMovie.getId();
                contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieId);
                contentValues.put(FavoritesContract.FavoritesEntry.COLUMNN_TITLE, movieTitle);
                contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_URL, posterUrl);
                Uri uri = getActivity().getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, contentValues);
                Log.v(LOG_TAG, "Added: " + uri.toString());
                Toast.makeText(getActivity(), "Movie " + movieTitle + " was added to favorites.", Toast.LENGTH_LONG).show();
                addFavoritesButton.setVisibility(View.INVISIBLE);
                removeFavoritesButton.setVisibility(View.VISIBLE);
            }
        });
        removeFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int taskDeleted;
                String selection = "movieId=?";
                movieId = selectedMovie.getId();
                String[] selectionArgs = new String[]{String.valueOf(movieId)};
                Uri uri = FavoritesContract.FavoritesEntry.CONTENT_URI;

                taskDeleted = getActivity().getContentResolver().delete(uri, selection, selectionArgs);
                Log.v(LOG_TAG, "removed: " + String.valueOf(taskDeleted));
                Toast.makeText(getActivity(), "Movie " + movieTitle + " was removed from favorites.", Toast.LENGTH_LONG).show();
                addFavoritesButton.setVisibility(View.VISIBLE);
                removeFavoritesButton.setVisibility(View.INVISIBLE);
            }
        });

        // Test what button to display
        String selection = "movieId=?";
        movieId = selectedMovie.getId();
        String[] selectionArgs = new String[]{String.valueOf(movieId)};
        Cursor cursor = getActivity().getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor.getCount() == 0) {
            addFavoritesButton.setVisibility(View.VISIBLE);
            removeFavoritesButton.setVisibility(View.INVISIBLE);
        } else if (cursor.getCount() > 0) {
            addFavoritesButton.setVisibility(View.INVISIBLE);
            removeFavoritesButton.setVisibility(View.VISIBLE);
        } else {
            Log.v(LOG_TAG, "Error with checking if the movie is already marked as favorite.");
        }
        return view;
    }
}
