package org.jkarsten.popularmovie.popularmovies.data.source;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.movielist.MovieListPresenter;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by juankarsten on 6/24/17.
 */

public interface MovieDataSource {
    void getFavoriteMovies(LoadMoviesCallback callback);
    void getPopularMovies(LoadMoviesCallback callback);
    void getTopRatedMovies(LoadMoviesCallback callback);
    void getPopularResponse(int page, LoadPopularResponseCallback callback);
    void getTopRatedResponse(int page, LoadTopRatedResponseCallback callback);
    void saveMovie(Movie movie);
    Observable<Movie> getMovie(int id);
    int getTotalPages();
    int getTotalResults();

    Observable<List<Movie>> createPopularResponseObservable(int page);
    Observable<List<Movie>> createTopRatedResponseObservable(int page);

    public static final int SORT_BY_POPULAR = 1;
    public static final int SORT_BY_TOP_RATED = 2;
    public static final int SORT_BY_FAVORITE = 3;

    interface LoadMoviesCallback {
        void onLoadedMovies(List<Movie> movies, int type);
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
}
