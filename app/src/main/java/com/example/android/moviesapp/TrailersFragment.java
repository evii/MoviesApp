package com.example.android.moviesapp;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.moviesapp.data.Trailer;
import com.example.android.moviesapp.data.TrailerAdapter;
import com.example.android.moviesapp.utilities.ApiClient;
import com.example.android.moviesapp.utilities.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailersFragment extends Fragment {

    ApiInterface apiInterface;
    private int movieId;
    private String API_KEY;
    private ListView mListView;
    private TrailerAdapter mAdapter;
    private List<Trailer> trailers;
    private static final String LOG_TAG = "Fragment_Trailers";

    public TrailersFragment() {
        //constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trailers_, container, false);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        API_KEY = this.getResources().getString(R.string.API_key);


        Call<Trailer.TrailersResults> trailerCall = apiInterface.getMovieTrailer(movieId, API_KEY);
        trailerCall.enqueue(new Callback<Trailer.TrailersResults>() {

            @Override
            public void onResponse(Call<Trailer.TrailersResults> call, Response<Trailer.TrailersResults> response) {
                trailers = response.body().getTrailersResults();

                mListView = view.findViewById(R.id.trailers_list_view);

                mAdapter = new TrailerAdapter(getActivity(), trailers);
                mListView.setAdapter(mAdapter);
              //  justifyListViewHeightBasedOnChildren(mListView);

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



        return view;
    }
}
