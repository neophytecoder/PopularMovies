package org.jkarsten.popularmovie.popularmovies;

import org.jkarsten.popularmovie.popularmovies.data.Movie;

/**
 * Created by juankarsten on 8/19/17.
 */

public interface OnMovieSelected {
    void onSelected(Movie movie, boolean initial);
}
