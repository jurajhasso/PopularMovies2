package com.example.juraj.popularmovies.util;

import android.content.Context;

import com.example.juraj.popularmovies.model.Movie;

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

}
