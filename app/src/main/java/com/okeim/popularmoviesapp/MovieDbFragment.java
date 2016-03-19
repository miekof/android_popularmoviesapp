package com.okeim.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
     * Calls FetchMoviesTask to update.
     */
    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask(mMovieArrayAdapter);
        movieTask.execute(getPref());
    }

    /**
     * Reads sort order user preference value from SharedPreference
     *
     * @return order preference string
     */
    private String getPref() {
        String displayPrefKey =
                PreferenceManager.getDefaultSharedPreferences(getActivity()).
                        getString(getString(R.string.pref_display_key),
                                getString(R.string.pref_display_toprated));

        return displayPrefKey;

    }

}
