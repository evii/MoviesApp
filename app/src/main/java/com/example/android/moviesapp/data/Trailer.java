package com.example.android.moviesapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by evi on 14. 3. 2018.
 */

public class Trailer {

    @SerializedName("name")
    private String mTrailerName;

    @SerializedName("key")
    private String mTrailerKey;

    public Trailer(String trailerName, String trailerKey) {
        mTrailerName = trailerName;
        mTrailerKey = trailerKey;
    }

    public String getTrailerName() {
        return mTrailerName;
    }

    public String getmTrailerKey() {
        return "https://www.youtube.com/watch?v=" + mTrailerKey;
    }

    public static class TrailersResults {
        @SerializedName("results")
        private List<Trailer> trailersResults;

        public List<Trailer> getTrailersResults() {
            return trailersResults;
        }
    }

}
