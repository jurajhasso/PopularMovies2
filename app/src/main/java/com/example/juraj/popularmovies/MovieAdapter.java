package com.example.juraj.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.juraj.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by juraj on 2/23/18.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context context;

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    private static ArrayList<Movie> moviesList = null;


    public MovieAdapter(Activity context, ArrayList<Movie> moviesList) {
        super(context, 0, moviesList);

        this.context = context;

    }

    public MovieAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ArrayList<Movie> getItems() {
        return moviesList;
    }

    public void setItems(ArrayList<Movie> mMoviesList) {
        moviesList = new ArrayList<Movie>();
        moviesList.clear();
        moviesList.addAll(mMoviesList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup viewGroup) {

        Movie movieItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_layout, viewGroup, false);
        }

        if (movieItem != null) {
            String posterPath = movieItem.getPoster_path();

            String fullPosterPath = BASE_IMAGE_URL + IMAGE_SIZE + posterPath;

            ImageView imageView = convertView.findViewById(R.id.image);

            Picasso.with(context).load(fullPosterPath).into(imageView);
        }

        return convertView;

    }
}
