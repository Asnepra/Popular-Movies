package com.example.ankit.popularmovies5;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ankit on 4/23/2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    Context mContext;
    ArrayList<TrailerData> trailerDataList;

    public TrailerAdapter(Context context, ArrayList<TrailerData> trailerDataList) {
        this.mContext = context;
        this.trailerDataList = trailerDataList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailers_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView trailerImage;
        CardView trailerCardView;

        public ViewHolder(View view) {
            super(view);
            trailerCardView = (CardView) view.findViewById(R.id.trailer_card_view);
            name = (TextView) view.findViewById(R.id.detail_activity_trailers_name);
            trailerImage = (ImageView) view.findViewById(R.id.detail_activity_trailer_image);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.trailerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, trailerDataList.get(position).getName(), Toast.LENGTH_LONG).show();
                Intent implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerDataList.get(position).getTrailerUrl()));
                mContext.startActivity(implicitIntent);
            }
        });
        holder.name.setText(trailerDataList.get(position).getName());
        Picasso.with(holder.trailerImage.getContext())
                .load(trailerDataList.get(position).getImageUrl())
                .into(holder.trailerImage);
    }


    @Override
    public int getItemCount() {
        return (null != trailerDataList ? trailerDataList.size() : 0);
    }
}
