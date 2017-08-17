package org.jkarsten.popularmovie.popularmovies.movielist;

import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteMovieDataSource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by juankarsten on 6/23/17.
 */

public class MovieListPresenter implements MovieListContract.Presenter, MovieDataSource.LoadMoviesCallback {
    private MovieListContract.View mView;
    private MovieDataSource mRepository;
    private int currentSort;

    public static final int SORT_BY_POPULAR = 1;
    public static final int SORT_BY_TOP_RATED = 2;
    public static final int SORT_BY_FAVORITE = 3;

    @Inject
    public MovieListPresenter(MovieListContract.View movieListView, MovieDataSource repository) {
        currentSort = SORT_BY_POPULAR;
        mView = movieListView;
        Log.d(MovieListPresenter.class.getSimpleName(), "created");
        mRepository = repository;
    }

    @Override
    public void start() {
        mView.showLoading();
        currentSort = mView.readSortingState();
        if (currentSort == MovieListPresenter.SORT_BY_POPULAR)
            mRepository.getPopularMovies(this);
        else if (currentSort == MovieListPresenter.SORT_BY_TOP_RATED)
            mRepository.getTopRatedMovies(this);
        else
            mRepository.getFavoriteMovies(this);
        Log.d(MovieListPresenter.class.getSimpleName(), "started");
    }

    @Override
    public void stop() {
        mView.writeSortingState(currentSort);

    }

    @Override
    public void viewMovie(Movie movie) {
        mView.goToMovieActivity(movie);
    }



    @Override
    public void onLoadedMovies(List<Movie> movies) {
        mView.hideLoading();
        mView.showMovies(movies);
    }

    @Override
    public void onDataNotAvailable() {
        mView.hideLoading();
        mView.showNoInternet();
    }

    @Override
    public void onPopularSelected() {
        currentSort = SORT_BY_POPULAR;
        mView.showLoading();
        mRepository.getPopularMovies(this);
    }

    @Override
    public void onTopRatedSelected() {
        currentSort = SORT_BY_TOP_RATED;
        mView.showLoading();
        mRepository.getTopRatedMovies(this);
    }

    @Override
    public void onFavoriteSelected() {
        currentSort = SORT_BY_FAVORITE;
        mView.showLoading();
        mRepository.getFavoriteMovies(this);
    }
}
