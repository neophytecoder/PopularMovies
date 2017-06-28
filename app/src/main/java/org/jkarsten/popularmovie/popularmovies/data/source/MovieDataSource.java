package org.jkarsten.popularmovie.popularmovies.data.source;

import org.jkarsten.popularmovie.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by juankarsten on 6/24/17.
 */

public interface MovieDataSource {
    interface LoadMoviesCallback {
        void onTaskLoaded(List<Movie> movies);
        void onDataUnavailable();
    }

    public List<Movie> getMovies();
}
