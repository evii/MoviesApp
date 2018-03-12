package com.example.android.moviesapp.utilities;

import com.example.android.moviesapp.data.Movie;
import com.example.android.moviesapp.data.Reviews;

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

   @GET("movie/{id}/reviews")
   Call<Reviews.ReviewsResult> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<Movie> getMovieTrailer(@Path("id") int id, @Query("api_key") String apiKey);
}


