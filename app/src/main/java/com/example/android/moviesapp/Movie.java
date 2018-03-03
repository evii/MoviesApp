package com.example.android.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by evi on 25. 2. 2018.
 */

public class Movie implements Parcelable {

    private String mTitle;
    private String mReleaseDate;
    private double mVoteAverage;
    private String mOverview;
    private String mPosterPath;

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[0];
        }
    };

    public Movie(String title, String releaseDate, double voteAverage, String overview, String posterPath) {
        mTitle = title;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mOverview = overview;
        mPosterPath = "http://image.tmdb.org/t/p/w500/" + posterPath;
    }

    public Movie(Parcel in) {
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readDouble();
        mOverview = in.readString();
        mPosterPath = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public double getVote() {
        return mVoteAverage;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mReleaseDate);
        parcel.writeDouble(mVoteAverage);
        parcel.writeString(mOverview);
        parcel.writeString(mPosterPath);
    }
}
