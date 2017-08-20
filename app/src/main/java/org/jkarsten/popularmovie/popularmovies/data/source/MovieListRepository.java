package org.jkarsten.popularmovie.popularmovies.data.source;

import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.MovieSortType;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.movielist.MovieListPresenter;

/**
 * Created by juankarsten on 6/24/17.
 */

public class MovieListRepository implements MovieDataSource,
        MovieDataSource.LoadPopularResponseCallback, MovieDataSource.LoadTopRatedResponseCallback {
    private static final int PAGE_ZERO = 0;
    private MovieDataSource mRemoteDataSource;
    private MovieDataSource mLocalDataSource;
    private LoadMoviesCallback mLoadMoviesCallback;

    public MovieListRepository(MovieDataSource remoteDataSource, MovieDataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
        // TODO: 6/29/17 deal with page number
    }

    @Override
    public void getPopularMovies(LoadMoviesCallback callback) {
        mLoadMoviesCallback = callback;
        mLocalDataSource.getPopularResponse(PAGE_ZERO, this);
    }

    @Override
    public void getTopRatedMovies(LoadMoviesCallback callback) {
        mLoadMoviesCallback = callback;
        mLocalDataSource.getTopRatedResponse(PAGE_ZERO, this);
    }

    @Override
    public void getFavoriteMovies(LoadMoviesCallback callback) {
        mLoadMoviesCallback = callback;
        mLocalDataSource.getFavoriteMovies(callback);
    }

    @Override
    public void getPopularResponse(final int page, final LoadPopularResponseCallback callback) {
        mLocalDataSource.getPopularResponse(page, new LoadPopularResponseCallback() {
            @Override
            public void onLoadPopularResponse(PopularResponse popularResponse) {
                if (popularResponse == null || popularResponse.getResults() == null
                        || popularResponse.getResults().size() == 0) {
                    Log.d(MovieListRepository.class.getSimpleName(), "get internet");
                    mRemoteDataSource.getPopularResponse(page, callback);
                } else {
                    callback.onLoadPopularResponse(popularResponse);
                }
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }


    @Override
    public void getTopRatedResponse(final int page, final LoadTopRatedResponseCallback callback) {
        mLocalDataSource.getTopRatedResponse(page, new LoadTopRatedResponseCallback() {
            @Override
            public void onLoadTopRatedResponse(TopRatedResponse topRatedResponse) {
                if (topRatedResponse == null || topRatedResponse.getResults() == null
                        || topRatedResponse.getResults().size() == 0) {
                    Log.d(MovieListRepository.class.getSimpleName(), "get internet");
                    mRemoteDataSource.getTopRatedResponse(page, callback);
                } else {
                    callback.onLoadTopRatedResponse(topRatedResponse);
                }
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public int getTotalPages() {
        return 0;
    }

    @Override
    public int getTotalResults() {
        return 0;
    }

    @Override
    public void onLoadPopularResponse(PopularResponse popularResponse) {
        mLoadMoviesCallback.onLoadedMovies(popularResponse.getResults(), MovieSortType.SORT_BY_POPULAR);
    }


    @Override
    public void onLoadTopRatedResponse(TopRatedResponse topRatedResponse) {
        mLoadMoviesCallback.onLoadedMovies(topRatedResponse.getResults(), MovieSortType.SORT_BY_TOP_RATED);
    }

    @Override
    public void onDataNotAvailable() {
        mLoadMoviesCallback.onDataNotAvailable();
    }

    @Override
    public void saveMovie(Movie movie) {
        mLocalDataSource.saveMovie(movie);
    }
}
