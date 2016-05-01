package com.example.ankit.popularmovies5;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean loadedOnce = false;
    RecyclerView mRecyclerView;
    final String popularMovieParam = "popularity.desc";
    final String highestRated = "vote_average.desc";
    public RequestQueue requestQueue;
    public ArrayList<MovieData> dataToBeFilled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadedOnce = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.detail_activity_comment_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (loadedOnce == false) {
            getPopularMovies(MainActivity.this, popularMovieParam);
        } else {
            mRecyclerView.setAdapter(new PosterAdapter(MainActivity.this, dataToBeFilled));
        }

    }

    public void getPopularMovies(final Context context, String param) {
        loadedOnce = true;
        requestQueue = Volley.newRequestQueue(context);
        dataToBeFilled = new ArrayList<>();
        String baseurl = "http://api.themoviedb.org/3/discover/movie?sort_by=" + param
                + "&&api_key=" + new KEY().getkey();
        final String baseimgurl = "http://image.tmdb.org/t/p/w342/";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(baseurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arrayItems = response.getJSONArray("results");
                            JSONObject movieObject;
                            for (int i = 0; i < arrayItems.length(); i++) {
                                movieObject = arrayItems.getJSONObject(i);
                                MovieData movie = new MovieData();
                                movie.id = movieObject.getInt("id");
                                movie.title = movieObject.getString("original_title");
                                movie.posterPath = baseimgurl + movieObject.getString("poster_path");
                                movie.releaseDate = movieObject.getString("release_date");
                                movie.overview = movieObject.getString("overview");
                                movie.ratings = (float) movieObject.getDouble("vote_average");
                                movie.voteCount = movieObject.getInt("vote_count");
                                dataToBeFilled.add(movie);
                            }
                            Toast.makeText(context, "Data Fetching Successful ", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(context, "Catch Error " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.setAdapter(new PosterAdapter(MainActivity.this, dataToBeFilled));

                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Parsing JSON " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
