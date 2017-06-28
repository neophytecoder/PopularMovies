package org.jkarsten.popularmovie.popularmovies.data.source;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;

import java.util.List;

/**
 * Created by juankarsten on 6/24/17.
 */

public interface MovieDataSource {
    interface LoadMoviesCallback {
        void onLoadedMovies(List<Movie> movies);
        void onDataNotAvailable();
    }

    interface LoadPopularResponseCallback {
        void onLoadPopularResponse(PopularResponse popularResponse);
        void onDataNotAvailable();
    }

    public void getMovies(LoadMoviesCallback callback);
    public void getPopularResponse(int page, LoadPopularResponseCallback callback);
    public int getTotalPages();
    public int getTotalResults();

}
