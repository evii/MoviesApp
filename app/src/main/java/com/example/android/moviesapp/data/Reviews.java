package com.example.android.moviesapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by evi on 12. 3. 2018.
 */

public class Reviews {

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    public Reviews(String author, String content){
        mAuthor = author;
        mContent = content;
    }
    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public static class ReviewsResult {
        @SerializedName("results")
        private List<Reviews> reviews;

        public List<Reviews> getReviewsList() {
            return reviews;
        }
    }

}
