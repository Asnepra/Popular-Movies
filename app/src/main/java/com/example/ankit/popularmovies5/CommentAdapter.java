package com.example.ankit.popularmovies5;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ankit on 4/21/2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context mContext;
    ArrayList<CommentData> commentDataList;

    public CommentAdapter(Context context, ArrayList<CommentData> commentDataList) {
        this.mContext = context;
        this.commentDataList = commentDataList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView author, description;
        ImageView poster;

        public ViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.commentors_name);
            description = (TextView) view.findViewById(R.id.commentors_review);
            poster = (ImageView) view.findViewById(R.id.movie_poster);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.author.setText(commentDataList.get(position).getAuthor());
        holder.description.setText(commentDataList.get(position).getDescription());
    }


    @Override
    public int getItemCount() {
        return (null != commentDataList ? commentDataList.size() : 0);
    }
}
