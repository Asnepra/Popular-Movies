package com.example.ankit.popularmovies5;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
 * Created by Ankit on 4/21/2016.
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder> {
    Context mContext;
    ArrayList<MovieData> movieData;

    public PosterAdapter(Context context, ArrayList<MovieData> movieData) {
        this.mContext = context;
        this.movieData = movieData;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_movie, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        ImageView poster;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardView);
            title = (TextView) view.findViewById(R.id.deatil_activity_movie_title);
            poster = (ImageView) view.findViewById(R.id.movie_poster);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, movieData.get(position).getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("MovieData", (Parcelable) movieData.get(position));
                ActivityOptions activityOptions = ActivityOptions.makeScaleUpAnimation(view, 0, 0,
                        view.getWidth(), view.getHeight());

                mContext.startActivity(intent, activityOptions.toBundle());
            }
        });
        Picasso.with(holder.poster.getContext())
                .load(movieData.get(position).getPosterPath())
                .placeholder(R.drawable.cute_girl1)
                .into(holder.poster);
        holder.title.setText(movieData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return (null != movieData ? movieData.size() : 0);
    }
}
