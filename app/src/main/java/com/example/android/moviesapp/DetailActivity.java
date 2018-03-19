package com.example.android.moviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.DbData.FavoritesContract;
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
    private Button addFavoritesButton;
    private Button removeFavoritesButton;
    private Movie selectedMovie;
    private int movieId;
    private String API_KEY;
    String movieTitle;

    private ListView mListView;
    private TrailerAdapter mAdapter;
    private List<Trailer> trailers;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mRecAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private static final String LOG_TAG = "DetailActivityFav";

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
        addFavoritesButton = findViewById(R.id.button_add_favorites);
        removeFavoritesButton = findViewById(R.id.button_remove_favorites);
        API_KEY = this.getResources().getString(R.string.API_key);


        // Movie details:
        Intent intent = getIntent();
        if (intent != null) {
            Bundle parcelableExtra = intent.getExtras();
            if (parcelableExtra != null) {
                if (parcelableExtra.containsKey("selectedMovie")) {
                    selectedMovie = intent.getParcelableExtra("selectedMovie");

                    int movieId = selectedMovie.getId();
                    ApiInterface apiInterface =
                            ApiClient.getClient().create(ApiInterface.class);

                    Call<Movie> movieDetailCall = apiInterface.getMovieDetail(movieId, API_KEY);
                    movieDetailCall.enqueue(new Callback<Movie>() {

                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            movieTitle = response.body().getTitle();
                            titleTV.setText(movieTitle);
                            setTitle(movieTitle);

                            String releaseDate =  response.body().getReleaseDate();
                            releaseTV.setText(releaseDate);

                            Double vote =  response.body().getVote();
                            String voteString = vote.toString();
                            voteTV.setText(voteString);

                            String synopsis =  response.body().getOverview();
                            synopsisTV.setText(synopsis);

                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Log.e(LOG_TAG, t.toString());
                        }
                    });

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
        movieId = selectedMovie.getId();
        ApiInterface apiInterface =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Reviews.ReviewsResult> reviewsCall = apiInterface.getMovieReviews(movieId, API_KEY);
        reviewsCall.enqueue(new Callback<Reviews.ReviewsResult>() {

            @Override
            public void onResponse(Call<Reviews.ReviewsResult> call, Response<Reviews.ReviewsResult> response) {
                List<Reviews> reviews = response.body().getReviewsList();
                TextView noReviewsTv = findViewById(R.id.no_review_tv);

                if (reviews.size() == 0) {
                    noReviewsTv.setVisibility(View.VISIBLE);

                } else {
                    noReviewsTv.setVisibility(View.INVISIBLE);
                    recyclerView = findViewById(R.id.reviews_rec_view);
                    mRecAdapter = new ReviewsAdapter(reviews);
                    layoutManager = new LinearLayoutManager(getApplicationContext());

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mRecAdapter);
                }
            }

            @Override
            public void onFailure(Call<Reviews.ReviewsResult> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });



        //Trailers:
        Call<Trailer.TrailersResults> trailerCall = apiInterface.getMovieTrailer(movieId, API_KEY);
        trailerCall.enqueue(new Callback<Trailer.TrailersResults>() {

            @Override
            public void onResponse(Call<Trailer.TrailersResults> call, Response<Trailer.TrailersResults> response) {
                trailers = response.body().getTrailersResults();

                mListView = findViewById(R.id.trailers_list_view);

                mAdapter = new TrailerAdapter(DetailActivity.this, trailers);
                mListView.setAdapter(mAdapter);
                justifyListViewHeightBasedOnChildren(mListView);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Trailer trailerSelected = trailers.get(position);
                        String url = trailerSelected.getmTrailerKey();
                        Intent intentTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intentTrailer);
                    }
                });
            }

            @Override
            public void onFailure(Call<Trailer.TrailersResults> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });

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
                Uri uri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, contentValues);
                Log.v(LOG_TAG, "Added: " + uri.toString());
                Toast.makeText(getBaseContext(), "Movie " + movieTitle + " was added to favorites.", Toast.LENGTH_LONG).show();
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

                taskDeleted = getContentResolver().delete(uri, selection, selectionArgs);
                Log.v(LOG_TAG, "removed: " + String.valueOf(taskDeleted));
                Toast.makeText(getBaseContext(), "Movie " + movieTitle + " was removed from favorites.", Toast.LENGTH_LONG).show();
                addFavoritesButton.setVisibility(View.VISIBLE);
                removeFavoritesButton.setVisibility(View.INVISIBLE);
            }
        });

        // Test what button to display
        String selection = "movieId=?";
        movieId = selectedMovie.getId();
        String[] selectionArgs = new String[]{String.valueOf(movieId)};
        Cursor cursor = getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor.getCount() == 0) {
            addFavoritesButton.setVisibility(View.VISIBLE);
            removeFavoritesButton.setVisibility(View.INVISIBLE);
        } else if (cursor.getCount() > 0) {
            addFavoritesButton.setVisibility(View.INVISIBLE);
            removeFavoritesButton.setVisibility(View.VISIBLE);
        } else {
            Log.v(LOG_TAG, "Error with checking if the movie is already marked as favorite.");
        }
    }

    // Helper method for trailers listView display. Resource: https://stackoverflow.com/questions/12212890/disable-scrolling-of-a-listview-contained-within-a-scrollview?noredirect=1&lq=1
    public static void justifyListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}

