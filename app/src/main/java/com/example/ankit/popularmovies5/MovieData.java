package com.example.ankit.popularmovies5;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ankit on 4/16/2016.
 */
public class MovieData implements Parcelable {
    int id;
    String title;
    String overview;
    String releaseDate;
    float ratings;
    int voteCount;
    String posterPath;

    public MovieData() {

    }

    public MovieData(int id, String title, String overview, String releaseDate,
                     float voteAverage, int voteCount, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.ratings = voteAverage;
        this.voteCount = voteCount;
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public MovieData(Parcel in) {
        title = in.readString();
        ratings = in.readFloat();
        voteCount = in.readInt();
        releaseDate = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        id = in.readInt();
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeFloat(ratings);
        dest.writeInt(voteCount);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeInt(id);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Float getRatings() {
        return ratings;
    }

    public String getReleasedate() {
        return releaseDate;
    }
}
