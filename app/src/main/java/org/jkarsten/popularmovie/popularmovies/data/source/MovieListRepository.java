package org.jkarsten.popularmovie.popularmovies.data.source;

import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;

/**
 * Created by juankarsten on 6/24/17.
 */

public class MovieListRepository implements MovieDataSource,
        MovieDataSource.LoadPopularResponseCallback, MovieDataSource.LoadTopRatedResponseCallback {
    private MovieDataSource mRemoteDataSource;
    private int currentPage = 1;
    private LoadMoviesCallback mLoadMoviesCallback;

    public MovieListRepository(MovieDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
        // TODO: 6/29/17  add local data source
        // TODO: 6/29/17 deal with page number
    }

    @Override
    public void getPopularMovies(LoadMoviesCallback callback) {
        mLoadMoviesCallback = callback;
        mRemoteDataSource.getPopularResponse(currentPage, this);
    }

    @Override
    public void getTopRatedMovies(LoadMoviesCallback callback) {
        mLoadMoviesCallback = callback;
        mRemoteDataSource.getTopRatedResponse(currentPage, this);
    }

    @Override
    public void getPopularResponse(int page, LoadPopularResponseCallback callback) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void getTopRatedResponse(int page, LoadTopRatedResponseCallback callback) {
        throw new UnsupportedOperationException();
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
        mLoadMoviesCallback.onLoadedMovies(popularResponse.getResults());
    }


    @Override
    public void onLoadTopRatedResponse(TopRatedResponse topRatedResponse) {
        mLoadMoviesCallback.onLoadedMovies(topRatedResponse.getResults());
    }

    @Override
    public void onDataNotAvailable() {
        mLoadMoviesCallback.onDataNotAvailable();
    }
}
