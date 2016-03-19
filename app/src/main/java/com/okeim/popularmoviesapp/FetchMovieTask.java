package com.okeim.popularmoviesapp;

import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask to download movie information from themoviedb.org and update the tile view.
 * Created by mieko on 3/19/2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    String displayPref;

    private MovieArrayAdapter mMovieArrayAdapter;

    /**
     * Constructor
     *
     * @param mMovieArrayAdapter
     */
    public FetchMovieTask(MovieArrayAdapter mMovieArrayAdapter) {
        this.mMovieArrayAdapter = mMovieArrayAdapter;
    }

    @Override
    protected Movie[] doInBackground(String... params) {

        //if there is no sort order pref, do nothing.
        if (params.length == 0) {
            return null;
        }
        displayPref = params[0];

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            
            final String API_KEY_PARAM = "api_key";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendEncodedPath("3")
                    .appendEncodedPath("movie")
                    .appendEncodedPath(displayPref)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();
            URL url = new URL(builder.toString());

            // Create the request to themoviedb.org, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getMovieFromJason(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    private Movie[] getMovieFromJason(String movieJsonStr)
            throws JSONException {
        Movie[] mMovieList = null;

        // There are the names of the JSON objects needs to be extracted
        final String MDB_RESULT = "results";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_ORIGINAL_TITLE = "original_title";
        final String MDB_SYNOPSIS = "overview";
        final String MDB_RATING = "vote_average";
        final String MDB_RELEASE_DATE = "release_date";

        //Image URL components
        final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";

        JSONObject resultJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = resultJson.getJSONArray(MDB_RESULT);

        mMovieList = new Movie[movieArray.length()];

        for (int i = 0; i < mMovieList.length; i++) {
            JSONObject movieJson = (JSONObject) movieArray.get(i);

            String posterPath = (String) movieJson.getString(MDB_POSTER_PATH);
            String originalTitle = (String) movieJson.getString(MDB_ORIGINAL_TITLE);
            String synopsis = (String) movieJson.getString(MDB_SYNOPSIS);
            String rating = (String) movieJson.getString(MDB_RATING);
            String releaseDate = (String) movieJson.getString(MDB_RELEASE_DATE);

            //Movie database adds a file separator at the beginning, remove it.
            if (posterPath.charAt(0) == '/') {
                posterPath = posterPath.substring(1);
            }

            Uri buildUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                    .appendEncodedPath(IMAGE_SIZE)
                    .appendEncodedPath(posterPath)
                    .build();
            mMovieList[i] = new Movie(originalTitle, buildUri.toString(), synopsis, rating, releaseDate);

        }
        return mMovieList;
    }

    @Override
    protected void onPostExecute(Movie[] result) {
        if (result != null) {
            mMovieArrayAdapter.clear();
            for (Movie mMovie : result) {
                mMovieArrayAdapter.add(mMovie);
            }

        }
    }
}