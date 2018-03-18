package com.example.android.moviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private ListView mListView;
    private TrailerAdapter mAdapter;
    private List<Trailer> trailers;

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
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                int movieId = selectedMovie.getId();
                String title = selectedMovie.getTitle();
                String posterUrl = selectedMovie.getPosterPath();
                contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieId);
                contentValues.put(FavoritesContract.FavoritesEntry.COLUMNN_TITLE, title);
                contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_URL, posterUrl);
                Uri uri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, contentValues);

                Toast.makeText(getBaseContext(), "Movie " + title + " was added to favorites.",Toast.LENGTH_LONG).show();
            }
        });


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

