package com.example.android.moviesapp;

/**
 * Created by evi on 25. 2. 2018.
 */

public class Movie {

    private String mTitle;
    private String mReleaseDate;
    private int mVoteAverage;
    private String mOverview;
    private String mPosterPath;

    public Movie (String title, String releaseDate, int voteAverage, String overview, String posterPath){
        mTitle=title;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mOverview = overview;
        mPosterPath = posterPath;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getReleaseDate(){
        return mReleaseDate;
    }

    public int getVote(){
        return mVoteAverage;
    }

    public String getOverview(){
        return mOverview;
    }

    public String getPosterPath(){
        return mPosterPath;
    }
}
