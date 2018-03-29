package com.example.juraj.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.juraj.popularmovies.model.MovieVideo;

import java.util.ArrayList;

/**
 * Created by juraj on 3/19/18.
 */

public class MovieVideoAdapter extends ArrayAdapter<MovieVideo> {

    private Context context;


    public MovieVideoAdapter(Activity context, ArrayList<MovieVideo> movieVideosList) {
        super(context, 0, movieVideosList);

        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup viewGroup) {

        MovieVideo videoItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.video_item_layout, viewGroup, false);
        }

        if (videoItem != null) {

            String video_key = videoItem.getKey();

            TextView keyTextView = convertView.findViewById(R.id.movie_video_link);

            //      keyTextView.setText(video_key);

            String video_name = videoItem.getName();

            TextView videoNameTextView = convertView.findViewById(R.id.movie_video_name);

            videoNameTextView.setText(video_name);

        }

        return convertView;

    }
}
