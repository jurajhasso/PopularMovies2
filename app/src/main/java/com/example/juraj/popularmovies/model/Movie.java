package com.example.juraj.popularmovies.model;


import android.os.Parcelable;
import android.os.Parcel;

/**
 * Created by juraj on 2/23/18.
 */

public class Movie implements Parcelable {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    private Integer id;
    private String title;
    private String release_date;
    private String poster_path;
    private Double vote_average;
    private String overview;

    public Movie () { }

    public Movie (Integer id, String title, String release_date, String poster_path, Double vote_average, String overview) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.overview = overview;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(release_date);
        parcel.writeString(poster_path);
        parcel.writeDouble(vote_average);
        parcel.writeString(overview);
    }

    public Movie(Parcel parcel){
        id = parcel.readInt();
        title = parcel.readString();
        release_date = parcel.readString();
        poster_path = parcel.readString();
        vote_average = parcel.readDouble();
        overview = parcel.readString();
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
