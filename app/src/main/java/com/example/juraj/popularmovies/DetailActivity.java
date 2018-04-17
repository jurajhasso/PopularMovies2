package com.example.juraj.popularmovies;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juraj.popularmovies.data.MoviesContract;
import com.example.juraj.popularmovies.model.Movie;
import com.example.juraj.popularmovies.model.MovieReview;
import com.example.juraj.popularmovies.model.MovieVideo;
import com.example.juraj.popularmovies.util.JsonUtil;
import com.example.juraj.popularmovies.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.juraj.popularmovies.data.MoviesDbHelper.LOG_TAG;

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

    private ListView mReviewListView;
    private ListView mVideoListView;
    public ScrollView mScrollView;
    private TextView mErrorMessageDisplay;

    public ArrayList<MovieReview> mReview;

    public ArrayList<MovieVideo> mVideo;

    private ImageButton mButton;
    private ContentResolver mContentResolver;

    Context context = DetailActivity.this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mReviewListView = (ListView) findViewById(R.id.movie_review_list);

        mVideoListView = (ListView) findViewById(R.id.movie_video_list);

        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message);

        final Intent intent = this.getIntent();

        int position = intent.getIntExtra(EXTRA_POSITION, 0);
        final int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, 0);

        final String movieIdString = String.valueOf(movieId);

        final String movieTitle = intent.getStringExtra(EXTRA_TITLE);
        final String releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE);
        final String posterPath = intent.getStringExtra(EXTRA_POSTER_PATH);
        final Double voteAverage = intent.getDoubleExtra(EXTRA_VOTE_AVERAGE, 0);

        final String voteAverageString = String.valueOf(voteAverage);
        final String movieOverview = intent.getStringExtra(EXTRA_MOVIE_OVERVIEW);

        final String fullPosterPath = BASE_IMAGE_URL + IMAGE_SIZE + posterPath;

        mContentResolver = DetailActivity.this.getContentResolver();

        mButton = (ImageButton) findViewById(R.id.favorite);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movieIdString);
                values.put(MoviesContract.MovieEntry.COLUMN_TITLE, movieTitle);
                values.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
                values.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);
                values.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAverageString);
                values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, movieOverview);

                Uri returned = mContentResolver.insert(MoviesContract.MovieEntry.CONTENT_URI, values);
                Log.d(LOG_TAG, "record id returned is " + returned.toString());
                Toast.makeText(context, "Marked as favorite!",
                        Toast.LENGTH_SHORT).show();
                mButton.setEnabled(false);
            }
        });

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

        if (savedInstanceState == null) {

            new FetchReviewsTask().execute(movieIdString);

            new FetchVideosTask().execute(movieIdString);

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<MovieReview>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<MovieReview> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String reviewQuery = String.valueOf(params[0]);
            URL ReviewRequestUrl = NetworkUtil.buildReviewUrl(reviewQuery);

            try {
                String jsonReviewResponse = NetworkUtil
                        .getResponseFromHttpUrl(ReviewRequestUrl);

                Log.v(TAG, "Review Response " + jsonReviewResponse);

                return
                        JsonUtil.parseReviewsFromJson(DetailActivity.this, jsonReviewResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<MovieReview> reviewData) {

            if (reviewData != null) {

                Log.v(TAG, " ReviewData " + reviewData);


                MovieReviewAdapter mMovieReviewAdapter = new MovieReviewAdapter((Activity) context, reviewData);

                mReview = reviewData;

                mMovieReviewAdapter.addAll(reviewData);

                ListView listView = findViewById(R.id.movie_review_list);
                listView.setAdapter(mMovieReviewAdapter);

            } else {
                showErrorMessage();
            }

        }
    }


    public class FetchVideosTask extends AsyncTask<String, Void, ArrayList<MovieVideo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<MovieVideo> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String videoQuery = String.valueOf(params[0]);
            URL MovieRequestUrl = NetworkUtil.buildVideoUrl(videoQuery);

            try {
                String jsonVideoResponse = NetworkUtil
                        .getResponseFromHttpUrl(MovieRequestUrl);

                Log.v(TAG, "Review Response " + jsonVideoResponse);

                return
                        JsonUtil.parseMovieVideosFromJson(DetailActivity.this, jsonVideoResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<MovieVideo> videoData) {

            if (videoData != null) {

                Log.v(TAG, " VideoData " + videoData);


                MovieVideoAdapter mMovieVideoAdapter = new MovieVideoAdapter((Activity) context, videoData);

                mMovieVideoAdapter.addAll(videoData);

                ListView listView = findViewById(R.id.movie_video_list);
                listView.setAdapter(mMovieVideoAdapter);

                mVideo = videoData;

                mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        MovieVideo data = videoData.get(position);

                        String key = data.getKey();

                        watchYoutubeVideo(context, key);

                    }
                });
            } else {
                showErrorMessage();
            }

        }
    }


    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }


    private void showErrorMessage() {

        mReviewListView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
