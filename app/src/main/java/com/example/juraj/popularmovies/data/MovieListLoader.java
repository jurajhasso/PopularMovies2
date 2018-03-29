package com.example.juraj.popularmovies.data;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.juraj.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by juraj on 3/27/18.
 */

public class MovieListLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private ArrayList<Movie> mMovie;
    private ContentResolver mContentResolver;
    private Cursor mCursor;

    public MovieListLoader(Context context, Uri uri, ContentResolver contentResolver) {
        super(context);
        mContentResolver = contentResolver;
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        String[] projection = {BaseColumns._ID,
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
                MoviesContract.MovieEntry.COLUMN_TITLE,
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
                MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
                MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MoviesContract.MovieEntry.COLUMN_OVERVIEW};

        ArrayList<Movie> entries = new ArrayList<Movie>();

        mCursor = mContentResolver.query(MoviesContract.MovieEntry.CONTENT_URI, projection, null, null, null);

        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    int id = mCursor.getInt(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID));
                    String title = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE));
                    String release_date = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE));
                    String poster_path = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH));

                    Log.v("POSTER PATH", "POSTER PATH FROM CURSOR" + poster_path);

                    Double vote_average = mCursor.getDouble(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE));
                    String overview = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW));

                    Movie movie = new Movie(id, title, release_date, poster_path, vote_average, overview);

                    entries.add(movie);

                } while (mCursor.moveToNext());
            }
        }

        Log.v("dsdas", "entries from loader" + entries);

        return entries;
    }

    @Override
    public void deliverResult(ArrayList<Movie> movie) {

        if (isReset()) {
            if (movie != null) {
                mCursor.close();
            }
        }

        Log.v("INSIDE OF DELIVER", "entries from loader" + movie);

        ArrayList<Movie> oldMovieList = mMovie;

        mMovie = movie;

        if (isStarted()) {
            super.deliverResult(movie);
        }

        Log.v("JUST CHECKING RIGHT", "entries from loader" + movie);

        if (oldMovieList != null && oldMovieList != movie) {
            mCursor.close();
        }

    }

    @Override
    protected void onStartLoading() {
        if (mMovie != null) {
            deliverResult(mMovie);
        }

        if (takeContentChanged() || mMovie == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mCursor != null) {
            mCursor.close();
        }

        mMovie = null;
    }

    @Override
    public void onCanceled(ArrayList<Movie> movie) {
        super.onCanceled(movie);
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}