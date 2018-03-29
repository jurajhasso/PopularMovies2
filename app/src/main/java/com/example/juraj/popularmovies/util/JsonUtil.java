package com.example.juraj.popularmovies.util;

import android.content.Context;

import com.example.juraj.popularmovies.model.Movie;
import com.example.juraj.popularmovies.model.MovieReview;
import com.example.juraj.popularmovies.model.MovieVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by juraj on 2/24/18.
 */

public class JsonUtil {
    public static ArrayList<Movie> parseMoviesFromJson(Context context, String jsonString) {

        ArrayList<Movie> movies = new ArrayList<>();

        try {

            JSONObject movieJson = new JSONObject(jsonString);

            JSONArray movieJsonArray = movieJson.getJSONArray("results");

            for(int i = 0;i < movieJsonArray.length();i++){

                JSONObject movieObject = movieJsonArray.getJSONObject(i);

                Integer id = movieObject.getInt("id");

                String title = movieObject.getString("title");

                String release_date = movieObject.getString("release_date");

                String poster_path = movieObject.getString("poster_path");

                Double vote_average = movieObject.getDouble("vote_average");

                String overview = movieObject.getString("overview");

                Movie movie = new Movie(id, title, release_date, poster_path, vote_average, overview);

                movies.add(movie);
            }

        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }

        return movies;
    }

    public static ArrayList<MovieReview> parseReviewsFromJson(Context context, String jsonString) {
        ArrayList<MovieReview> reviews = new ArrayList<>();

        try {

            JSONObject reviewJson = new JSONObject(jsonString);

            JSONArray reviewJsonJSONArray = reviewJson.getJSONArray("results");

            for (int i = 0; i < reviewJsonJSONArray.length(); i++) {

                JSONObject reviewObject = reviewJsonJSONArray.getJSONObject(i);

                String content = reviewObject.getString("content");

                String author = reviewObject.getString("author");

                MovieReview review = new MovieReview(content, author);

                reviews.add(review);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return reviews;

    }

    public static ArrayList<MovieVideo> parseMovieVideosFromJson(Context context, String jsonString) {

        ArrayList<MovieVideo> videos = new ArrayList<>();

        try {

            JSONObject videoJson = new JSONObject(jsonString);

            JSONArray videoJsonJSONArray = videoJson.getJSONArray("results");

            for (int i = 0; i < videoJsonJSONArray.length(); i++) {

                JSONObject videoObject = videoJsonJSONArray.getJSONObject(i);

                String key = videoObject.getString("key");

                String name = videoObject.getString("name");

                MovieVideo movieVideo = new MovieVideo(key, name);

                videos.add(movieVideo);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return videos;

    }

}
