package com.okeim.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * MovieDbFragment
 */
public class MovieDbFragment extends Fragment {

    private ArrayList<Movie> movieArray = new ArrayList<Movie>(); //List of movie poster image URI
    private MovieArrayAdapter mMovieArrayAdapter; //Movie Array Adapter

    public MovieDbFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        mMovieArrayAdapter = new MovieArrayAdapter(getActivity(), movieArray);
        gridView.setAdapter(mMovieArrayAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie selectedMovie = mMovieArrayAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(getString(R.string.extra_key_movie), selectedMovie);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviedbfragment, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    /**
     * Calls FetchMoviesTask.
     */
    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
    }

    /**
     *
     */
    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        String displayPref;

        @Override
        protected Movie[] doInBackground(String... params) {

            //Read the display sort order preference from the SharedPreferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String displayPrefKey = prefs.getString(getString(R.string.pref_display_key),
                    getString(R.string.pref_display_toprated));

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {


                //To obtain movie information
                final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";
                final String MOVIE_POPULAR = "popular";
                final String MOVIE_TOPRATED = "top_rated";

                if (displayPrefKey.equals(getString(R.string.pref_display_toprated))) {
                    displayPref = MOVIE_TOPRATED;
                } else {
                    displayPref = MOVIE_POPULAR;
                }
                Uri buildUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendEncodedPath(displayPref)
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                        .build();
                URL url = new URL(buildUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
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
}
