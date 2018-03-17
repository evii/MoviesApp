package com.example.android.moviesapp.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesapp.R;

import java.util.List;

/**
 * Created by evi on 15. 3. 2018.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    Context mContext = getContext();
    private List<Trailer> mTrailers;

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super(context, 0, trailers);
        mTrailers = trailers;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.trailer_list_item, parent, false);
        TextView trailerNameTv = itemView.findViewById(R.id.trailer_name_tv);
        trailerNameTv.setText(mTrailers.get(position).getTrailerName());

        return itemView;
    }

}
