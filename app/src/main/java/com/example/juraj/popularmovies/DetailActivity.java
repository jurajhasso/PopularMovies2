package com.example.juraj.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juraj.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by juraj on 2/23/18.
 */

public class DetailActivity extends Activity {

    public static final String EXTRA_POSITION = "extra_position";

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    public static final String EXTRA_TITLE = "extra_title";

    public static final String EXTRA_RELEASE_DATE = "extra_release_date";

    public static final String EXTRA_POSTER_PATH = "extra_poster_path";

    public static final String EXTRA_VOTE_AVERAGE = "extra_vote_average";

    public static final String EXTRA_MOVIE_OVERVIEW = "extra_movie_overview";

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185/";

    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.context = context;

        Intent intent = this.getIntent();

        int position = intent.getIntExtra(EXTRA_POSITION, 0);
        int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, 0);
        String movieTitle = intent.getStringExtra(EXTRA_TITLE);
        String releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE);
        String posterPath = intent.getStringExtra(EXTRA_POSTER_PATH);
        Double voteAverage = intent.getDoubleExtra(EXTRA_VOTE_AVERAGE,0);
        String movieOverview = intent.getStringExtra(EXTRA_MOVIE_OVERVIEW);

        String fullPosterPath = BASE_IMAGE_URL + IMAGE_SIZE + posterPath;

        ImageView imageView = findViewById(R.id.image_detail);

        Picasso.with(context).load(fullPosterPath).into(imageView);

        TextView movieDetailTitle = findViewById(R.id.movie_title_tv);

        TextView movieReleaseDate = findViewById(R.id.movie_release_date_tv);

        TextView movieVoteAverage = findViewById(R.id.movie_vote_average);

        TextView movieOverView = findViewById(R.id.movie_overview_tv);

        movieDetailTitle.setText(movieTitle);

        movieReleaseDate.setText(releaseDate);

        String votAverageString = voteAverage.toString();

        movieVoteAverage.setText(votAverageString);

        if (voteAverage >= 8.0) {
            movieVoteAverage.setTextColor(Color.rgb(10, 150, 57));
        }

        else if (voteAverage >= 5.0 && voteAverage < 8.0) {
            movieVoteAverage.setTextColor(Color.BLACK);
        }

        else if (voteAverage < 5.0) {
            movieVoteAverage.setTextColor(Color.RED);
        }

        movieOverView.setText(movieOverview);

    }

    private void closeOnError () {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }
}
