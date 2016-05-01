package com.example.ankit.popularmovies5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    RecyclerView mCommentRecyclerView, mTrailerRecyclerView;
    RequestQueue requestQueue;
    MovieData data;
    TextView title, synopsis, release_date, ratings, trailerTextView;
    RatingBar rbar;
    ImageView trailerImage, poster;
    String trailerFinalUrl, key, trailerName, baseurl, trailerImageUrl;
    ArrayList<CommentData> dataToBeFilled;
    ArrayList<TrailerData> trailerDataToBeFilled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        data = getIntent().getParcelableExtra("MovieData");
        mCommentRecyclerView = (RecyclerView) findViewById(R.id.detail_activity_comment_recycler_view);
        //mTrailerRecyclerView = (RecyclerView) findViewById(R.id.deatil_activity_trailer_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
        populate();
        getReviews();
        mCommentRecyclerView.setAdapter(new CommentAdapter(DetailActivity.this, dataToBeFilled));
        getTrailer();
        //mTrailerRecyclerView.setAdapter(new TrailerAdapter(DetailActivity.this, trailerDataToBeFilled));
    }

    private void initialize() {


        title = (TextView) findViewById(R.id.deatil_activity_movie_title);
        poster = (ImageView) findViewById(R.id.detail_activity_main_poster);
        synopsis = (TextView) findViewById(R.id.detail_activity_synopsis);
        release_date = (TextView) findViewById(R.id.detail_activity_release_date);
        ratings = (TextView) findViewById(R.id.detail_activity_rating);
        rbar = (RatingBar) findViewById(R.id.detail_activity_ratingbar);
        trailerTextView = (TextView) findViewById(R.id.detail_activity_trailers_name);
        // fab = (ImageButton) findViewById(R.id.fab);
        trailerImage = (ImageView) findViewById(R.id.detail_activity_trailer_image);
    }

    public void populate() {
        //Set the movie name in app bar
        // setTitle(data.getTitle());
        //set Title
        title.setText(data.getTitle());

        Picasso.with(getBaseContext()).load(data.getPosterPath())
                .placeholder(R.drawable.cute_girl1)
                .into(poster);
        synopsis.setText("Overview\n" + "\n" + data.overview);

        rbar.setRating(data.getRatings() / 2f);

        ratings.setText("Ratings: " + (float) Math.round(data.ratings * 10d) / 10d + "/10");

        SimpleDateFormat output = new SimpleDateFormat("dd MMM, yyyy");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        String releasedDate;
        try {
            releasedDate = output.format(input.parse(data.releaseDate));
        } catch (Exception e) {
            e.printStackTrace();
            releasedDate = data.releaseDate;
        }
        release_date.setText("Release date: " + releasedDate);
        //Testing for movie id
        //movie_id_test_trailers.setText("Movie's id is " + data.id);
        animateContent();

        //fab.setOnClickListener(this);
    }

    public void animateContent() {
        View[] animatedViews = new View[]{
                title, release_date, ratings, synopsis, poster
        };
        Interpolator interpolator = new DecelerateInterpolator();
        for (int i = 0; i < animatedViews.length; ++i) {
            View v = animatedViews[i];
            v.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            v.setAlpha(0f);
            v.setTranslationY(75);
            v.animate()
                    .setInterpolator(interpolator)
                    .alpha(1.0f)
                    .translationY(0)
                    .setStartDelay(150 + 75 * i)
                    .start();
        }
    }

    public void getReviews() {
        dataToBeFilled = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getBaseContext());
        int movie_id = data.id;
        baseurl = "http://api.themoviedb.org/3/movie/" + movie_id + "/reviews"
                + "?api_key=" + new KEY().getkey();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(baseurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("results");
                            JSONObject commentObjectt;
                            for (int i = 0; i < items.length(); i++) {
                                commentObjectt = items.getJSONObject(i);
                                CommentData comment = new CommentData();
                                comment.author = commentObjectt.getString("author");
                                comment.description = commentObjectt.getString("content");
                                dataToBeFilled.add(comment);
                            }

                            Toast.makeText(DetailActivity.this, "Comments Fetch Sucessfull ", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            //Log.e(LOG_TAG, e.toString());
                            Toast.makeText(DetailActivity.this, "Catch Error Comments Fetch  " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCommentRecyclerView.setAdapter(new CommentAdapter(DetailActivity.this, dataToBeFilled));
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d(LOG_TAG, "error parsing JSON");
                Toast.makeText(DetailActivity.this, "Error Parsing JSON Comment", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getTrailer() {
        requestQueue = Volley.newRequestQueue(getBaseContext());
        int movie_id = data.id;
        baseurl = "http://api.themoviedb.org/3/movie/" + movie_id + "/videos"
                + "?api_key=" + new KEY().getkey();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(baseurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("results");
                            JSONObject movie_trailer_object;
                            for (int i = 0; i < items.length(); i++) {
                                movie_trailer_object = items.getJSONObject(i);
                                TrailerData trailerData = new TrailerData();
                                trailerData.name = data.title + movie_trailer_object.getString("name");
                                trailerName = movie_trailer_object.getString("name");
                                key = movie_trailer_object.getString("key");
                                trailerFinalUrl = "https://www.youtube.com/watch?v=" + key;
                                trailerImageUrl = "http://img.youtube.com/vi/" + key + "/hqdefault.jpg";
                                trailerData.trailerUrl = trailerFinalUrl;
                                trailerData.imageUrl = trailerImageUrl;
                                //trailerDataToBeFilled.add(trailerData);
                                //siteName = movie_trailer_object.getString("site");
                            }
                            Toast.makeText(DetailActivity.this, "Trailer Fetch Succesful ", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            //Log.e(LOG_TAG, e.toString());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //siteName = siteName.toLowerCase();
                                //mTrailerRecyclerView.setAdapter(new TrailerAdapter(DetailActivity.this, trailerDataToBeFilled));
                                trailerFinalUrl = "https://www.youtube.com/watch?v=" + key;
                                trailerImageUrl = "http://img.youtube.com/vi/" + key + "/default.jpg";
                                //Toast.makeText(getBaseContext(), "Call", Toast.LENGTH_SHORT).show();
                                //trailerTextView.setText("Trailers info\n" + trailerFinalUrl);
                                //Loading the Thumbnail of trailer
                                trailerTextView.setText(trailerName);
                                Picasso.with(DetailActivity.this)
                                        .load(trailerImageUrl)
                                        .placeholder(R.drawable.cute_girl1)
                                        .into(trailerImage);
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d(LOG_TAG, "error parsing JSON");
                Toast.makeText(DetailActivity.this, "Error Parsing JSON trailer", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void launchTrailer(View view) {
        if (trailerFinalUrl.isEmpty()) {
            Toast.makeText(DetailActivity.this, "Please wait for the trailer to be loaded ", Toast.LENGTH_LONG).show();
        } else {
            Intent implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerFinalUrl));
            startActivity(implicitIntent);
        }
        //Toast.makeText(getBaseContext(), trailerFinalUrl, Toast.LENGTH_SHORT).show();

    }

}
