package com.okeim.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private Movie selectedMovie;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(getString(R.string.extra_key_movie))) {
            selectedMovie = (Movie) intent.getExtras().getParcelable(getString(R.string.extra_key_movie));
            //Set the title text
            ((TextView) rootView.findViewById(R.id.detail_text_title))
                    .setText(selectedMovie.getMovieTitle());
            //set the poster image
            Picasso.with(getActivity()).
                    load(selectedMovie.getMovieFileUri()).
                    into((ImageView) rootView.findViewById(R.id.detail_image_poster));
            //Set rating text
            ((TextView) rootView.findViewById(R.id.detail_text_rating)).
                    setText(getString(R.string.detail_label_rating) + selectedMovie.getUserRating());
            //set release date text
            ((TextView) rootView.findViewById(R.id.detail_text_releasedate)).
                    setText(getString(R.string.detail_label_releasedate) + selectedMovie.getReleaseDate());
            //set synopsis text
            ((TextView) rootView.findViewById(R.id.detail_text_synopsis)).setText(selectedMovie.getSynopsis());
        }

        return rootView;
    }
}
