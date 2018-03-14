package com.example.android.moviesapp.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviesapp.R;

import java.util.List;

/**
 * Created by evi on 12. 3. 2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private static final String TAG = ReviewsAdapter.class.getSimpleName();
    private List<Reviews> mReviews;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView authorTv;
        public TextView contentTv;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            authorTv = v.findViewById(R.id.author_tv);
            contentTv = v.findViewById(R.id.review_content_tv);
        }
    }

    public ReviewsAdapter(List<Reviews> reviews) {
        mReviews = reviews;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.review_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            Reviews reviewItem = mReviews.get(position);
        String author = reviewItem.getAuthor();
        String content = reviewItem.getContent();
        holder.authorTv.setText(author);
        holder.contentTv.setText(content);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}




















