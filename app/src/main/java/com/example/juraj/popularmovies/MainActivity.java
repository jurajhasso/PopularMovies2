package com.example.juraj.popularmovies;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.juraj.popularmovies.data.MovieListLoader;
import com.example.juraj.popularmovies.data.MoviesContract;
import com.example.juraj.popularmovies.model.Movie;
import com.example.juraj.popularmovies.util.JsonUtil;
import com.example.juraj.popularmovies.util.NetworkUtil;

import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static final String GRID_VIEW_POSITION = "POSITION";

    private static final String INDEX = "index";

    private Parcelable mGridState = null;

    private int mIndex;

    private static final int LOADER_ID = 1;

    private TextView mErrorMessageDisplay;

    private TextView mInternetErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private GridView mGridView;

    private static final String DEFAULT_QUERY_TYPE = "movie/popular";

    private String type = DEFAULT_QUERY_TYPE;

    Context context = MainActivity.this;

    private ContentResolver mContentResolver;

    public ArrayList<Movie> mMovie;

    private MovieAdapter mMovieAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message);

        mInternetErrorMessageDisplay = (TextView) findViewById(R.id.error_internet_message);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        mGridView = (GridView) findViewById(R.id.gridview);

        mContentResolver = MainActivity.this.getContentResolver();

        if (savedInstanceState == null) {

            getLoaderManager().initLoader(LOADER_ID, null, this);

        }


        if (savedInstanceState == null) {

            loadMovieData();

        }
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        ArrayList<Movie> items = savedInstanceState.getParcelableArrayList("myParceledList");

        Log.v("items that returned", "items that returned" + items);

        mIndex = savedInstanceState.getInt(INDEX);

        mMovieAdapter = new MovieAdapter(context, 0);

        if (items != null) {

            mMovieAdapter.addAll(items);

            Log.v("index from oncreate", "index from oncreate" + mIndex);

            mGridView.setAdapter(mMovieAdapter);

            mGridView.setSelection(mIndex);
        } else {

            loadMovieData();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

        Log.v("onsaveitems", "onsaveitems" + mMovie);

        savedInstanceState.putParcelableArrayList("myParceledList", mMovie);

        mIndex = mGridView.getFirstVisiblePosition();

        savedInstanceState.putInt(INDEX, mIndex);

    }


    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, Bundle args) {
        mContentResolver = context.getContentResolver();
        return new MovieListLoader(context, MoviesContract.MovieEntry.CONTENT_URI, mContentResolver);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, final ArrayList<Movie> movieData) {
        getLoaderManager().destroyLoader(LOADER_ID);
        MovieAdapter mMovieAdapter = new MovieAdapter((Activity) context, movieData);

        //  mMovieAdapter.addAll(movieData);

        GridView gridview = findViewById(R.id.gridview);
        gridview.setAdapter(mMovieAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie data = movieData.get(position);

                Integer movieId = data.getId();
                String movieTitle = data.getTitle();
                String movieReleaseDate = data.getRelease_date();
                String moviePosterPath = data.getPoster_path();
                Double movieVoteAverage = data.getVote_average();
                String movieOverview = data.getOverview();

                startDetailActivity(position, movieId, movieTitle, movieReleaseDate, moviePosterPath, movieVoteAverage, movieOverview);

            }
        });

    }

    @Override
    public void onLoaderReset(Loader<
            ArrayList<Movie>> loader) {
        mMovieAdapter.add(null);
    }


    private void loadMovieData() {

        if (isConnected(context)) {

            showMovieDataView();

        new FetchMoviesTask(new AsyncResponse(){

            @Override
            public void processFinish(ArrayList<Movie> movieList){

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("value", movieList);
                intent.putExtras(bundle);

                    Log.v(TAG, "movie list onprocess finish" + movieList);
                }

        }).execute(type);

    } else {
            showInternetErrorMessage();
        }

}

    public static boolean isConnected(Context context )
    {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showMovieDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mInternetErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mGridView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        mGridView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showInternetErrorMessage() {

        mGridView.setVisibility(View.INVISIBLE);

        mInternetErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void startDetailActivity(int position, int movieId, String movieTitle, String movieReleaseDate, String moviePosterPath, Double movieVoteAverage, String movieOverview) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, movieId);
        intent.putExtra(DetailActivity.EXTRA_TITLE, movieTitle);
        intent.putExtra(DetailActivity.EXTRA_RELEASE_DATE, movieReleaseDate);
        intent.putExtra(DetailActivity.EXTRA_POSTER_PATH, moviePosterPath);
        intent.putExtra(DetailActivity.EXTRA_VOTE_AVERAGE, movieVoteAverage);
        intent.putExtra(DetailActivity.EXTRA_MOVIE_OVERVIEW, movieOverview);

        startActivity(intent);
    }

    public interface AsyncResponse {
        void processFinish(ArrayList<Movie> movieData);
    }

    public final class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        public AsyncResponse delegate = null;

        public FetchMoviesTask(AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String type = params[0];
            URL MovieRequestUrl = NetworkUtil.buildUrl(type);

            try {
                String jsonMovieResponse = NetworkUtil
                        .getResponseFromHttpUrl(MovieRequestUrl);

                Log.v(TAG, "Response " + jsonMovieResponse);

                return
                        JsonUtil.parseMoviesFromJson(MainActivity.this, jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<Movie> movieData) {

            delegate.processFinish(movieData);

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {

                Log.v(TAG, "Data " + movieData);

                MovieAdapter mMovieAdapter = new MovieAdapter((Activity) context, movieData);

                mMovieAdapter.addAll(movieData);

                mMovie = movieData;

                Log.v(TAG, "Data " + mMovie);

                GridView gridview = findViewById(R.id.gridview);

                gridview.setAdapter(mMovieAdapter);

                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Movie data = movieData.get(position);

                        Integer movieId = data.getId();
                        String movieTitle = data.getTitle();
                        String movieReleaseDate = data.getRelease_date();
                        String moviePosterPath = data.getPoster_path();
                        Double movieVoteAverage = data.getVote_average();
                        String movieOverview = data.getOverview();

                        startDetailActivity(position, movieId, movieTitle, movieReleaseDate, moviePosterPath, movieVoteAverage, movieOverview);

                    }
                });


            } else {
                showErrorMessage();
            }
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.most_popular:
                type = "movie/popular";
                loadMovieData();
                return true;
            case R.id.highest_rated:
                type = "movie/top_rated";
                loadMovieData();
                return true;
            case R.id.favorites:
                getLoaderManager().initLoader(LOADER_ID, null, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
