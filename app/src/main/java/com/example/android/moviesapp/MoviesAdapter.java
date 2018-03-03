package com.example.android.moviesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by evi on 25. 2. 2018.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {
    Context mContext = getContext();

    public MoviesAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(movie.getPosterPath()).into(imageView);

        return imageView;
    }
}