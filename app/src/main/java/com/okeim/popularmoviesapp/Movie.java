package com.okeim.popularmoviesapp;

/**
 * Movie holds information about each movies; title, post URI, synopsis, user rating and release date.
 *
 * Created by mieko on 3/17/2016.
 */
public class Movie {

    private String movieTitle; //original title
    private String movieFileUri; //URL of movie poster image thumbnail
    private String synopsis; //A plot synopsis (called overview in the api)
    private String userRating; //user rating (called vote_average in the api)
    private String releaseDate; //movie release date

    /**
     * Constructor
     * @param movieTitle
     * @param movieFileUri
     * @param synopsis
     * @param userRating
     * @param releaseDate
     */
    public Movie(String movieTitle, String movieFileUri, String synopsis, String userRating, String releaseDate) {
        this.movieTitle = movieTitle;
        this.movieFileUri = movieFileUri;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    /**
     * Returns movie title
     * @return movieTitle
     */
    public String getMovieTitle() {
        return movieTitle;
    }

    /**
     * Sets the movie title
     * @param movieTitle
     */
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }


    /**
     * Returns movie file URI
     * @return
     */
    public String getMovieFileUri() {
        return movieFileUri;
    }

    /**
     * Sets movie file URI
     * @param movieFileUri
     */
    public void setMovieFileUri(String movieFileUri) {
        this.movieFileUri = movieFileUri;
    }

    /**
     * Returns synopsis
     * @return
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * Sets the synopsis
     * @param synopsis
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * Returns User Rating
     * @return
     */
    public String getUserRating() {
        return userRating;
    }

    /**
     * Sets the User Rating
     * @param userRating
     */
    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    /**
     * Returns release date
     * @return
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * sets the release date
     * @param releaseDate
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
