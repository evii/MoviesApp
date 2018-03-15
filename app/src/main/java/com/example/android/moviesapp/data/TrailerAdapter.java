package com.example.android.moviesapp.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviesapp.R;

import java.util.List;

/**
 * Created by evi on 15. 3. 2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailer> mTrailers;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView trailerNameTv;
        public View trailerLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerLayout = itemView;
            trailerNameTv = itemView.findViewById(R.id.trailer_name_tv);
        }
    }

    public TrailerAdapter(List<Trailer> trailers) {
        mTrailers = trailers;
    }


    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.trailer_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        Trailer trailerItem = mTrailers.get(position);
        String trailerName = trailerItem.getTrailerName();
        holder.trailerNameTv.setText(trailerName);


    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}
