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
        void onLoadTopRatedResponse(TopRatedResponse popularResponse);
        void onDataNotAvailable();
    }

     void getPopularMovies(LoadMoviesCallback callback);
     void getTopRatedMovies(LoadMoviesCallback callback);
     void getPopularResponse(int page, LoadPopularResponseCallback callback);
     void getTopRatedResponse(int page, LoadTopRatedResponseCallback callback);
    void saveMovie(Movie movie);
     int getTotalPages();
     int getTotalResults();

}
