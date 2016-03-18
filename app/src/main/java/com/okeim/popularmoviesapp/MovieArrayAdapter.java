package com.okeim.popularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * MovieArrayAdapter
 *
 * MovieArrayAdapter extends ArrayAdapter.
 * This works similar to ArrayAdapter, instead of displaying strings
 * this takes list of image URL's and displays them into ImageView.
 *
 * This section of code ıs created based on
 * https://github.com/udacity/android-custom-arrayadapter/blob/parcelable/app/src/main/java/demo/example/com/customarrayadapter/AndroidFlavorAdapter.java
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private Activity context;

    /**
     * Constructor
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param movies      A list of image uri to display in a grid
     */
    public MovieArrayAdapter(Activity context, ArrayList<Movie> movies) {
        super(context, R.layout.grid_item_movie, movies);
        this.context = context;

    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }
        Picasso.with(context).load(getItem(position).getMovieFileUri()).into((ImageView) convertView);
        return convertView;
    }
}