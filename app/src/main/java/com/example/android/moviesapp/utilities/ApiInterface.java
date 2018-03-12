package com.example.android.moviesapp.utilities;

import com.example.android.moviesapp.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by evi on 12. 3. 2018.
 */

public interface ApiInterface {
    @GET("movie/top_rated")
    Call<Movie.MovieResult> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<Movie.MovieResult> getPopularMovies(@Query("api_key") String apiKey);

  //  @GET("movie/{id}")
  //  Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}