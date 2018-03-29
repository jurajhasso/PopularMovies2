package com.example.juraj.popularmovies.util;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by juraj on 2/24/18.
 */

public final class NetworkUtil {

    private static final String api_key = "";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private final static String QUERY_DELIMITER = "api_key";

    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";

    public static final String REVIEW_API_DELIMITER = "reviews";

    public static final String VIDEO_API_DELIMITER = "videos";

    public static URL buildUrl(String typeQuery) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(typeQuery)
                .appendQueryParameter(QUERY_DELIMITER, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildReviewUrl(String reviewQuery) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendEncodedPath(reviewQuery)
                .appendEncodedPath(REVIEW_API_DELIMITER)
                .appendQueryParameter(QUERY_DELIMITER, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built review URI " + url);

        return url;
    }

    public static URL buildVideoUrl(String videoQuery) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendEncodedPath(videoQuery)
                .appendEncodedPath(VIDEO_API_DELIMITER)
                .appendQueryParameter(QUERY_DELIMITER, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built video URI " + url);

        return url;
    }




    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);

            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
