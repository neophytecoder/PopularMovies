package org.jkarsten.popularmovie.popularmovies.data.source;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;

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

    interface LoadTopRatedResponseCallback {
        void onLoadPopularResponse(TopRatedResponse popularResponse);
        void onDataNotAvailable();
    }

    public void getPopularMovies(LoadMoviesCallback callback);
    public void getTopRatedMovies(LoadMoviesCallback callback);
    public void getPopularResponse(int page, LoadPopularResponseCallback callback);
    public void getTopRatedResponse(int page, LoadPopularResponseCallback callback);
    public int getTotalPages();
    public int getTotalResults();

}
