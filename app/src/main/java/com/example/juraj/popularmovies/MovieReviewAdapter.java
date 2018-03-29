package com.example.juraj.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.juraj.popularmovies.model.MovieReview;

import java.util.ArrayList;

/**
 * Created by juraj on 3/19/18.
 */

public class MovieReviewAdapter extends ArrayAdapter<MovieReview> {

    private Context context;


    public MovieReviewAdapter(Activity context, ArrayList<MovieReview> movieReviewsList) {
        super(context, 0, movieReviewsList);

        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup viewGroup) {

        MovieReview reviewItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item_layout, viewGroup, false);
        }

        if (reviewItem != null) {

            String content = reviewItem.getContent();

            TextView contentTextView = convertView.findViewById(R.id.movie_review_tv);

            contentTextView.setText(content);

            String author = reviewItem.getAuthor();

            TextView authorTextView = convertView.findViewById(R.id.movie_review_author_tv);

            authorTextView.setText(author);

        }

        return convertView;

    }
}
