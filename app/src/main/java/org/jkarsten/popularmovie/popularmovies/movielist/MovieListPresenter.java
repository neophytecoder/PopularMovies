package org.jkarsten.popularmovie.popularmovies.movielist;

import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.OnMovieSelected;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.MovieSortType;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteMovieDataSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by juankarsten on 6/23/17.
 */

public class MovieListPresenter implements MovieListContract.Presenter, MovieDataSource.LoadMoviesCallback {
    private MovieListContract.View mView;
    private MovieDataSource mRepository;
    private int currentSort;

    private boolean initialView;
    private boolean mDualPane;
    private OnMovieSelected mOnMovieSelected;
    private List<Movie> mMovies;


    @Inject
    public MovieListPresenter(MovieListContract.View movieListView, MovieDataSource repository) {
        currentSort = MovieSortType.SORT_BY_POPULAR;
        mView = movieListView;
        Log.d(MovieListPresenter.class.getSimpleName(), "created");
        mRepository = repository;
    }

    @Override
    public void start() {
        mView.showLoading();
        initialView = true;

        mDualPane = mView.isDualPane();
        if (mDualPane) {
            mOnMovieSelected = mView.getOnMovieSelected();
        }

        currentSort = mView.readSortingState();
        if (currentSort == MovieSortType.SORT_BY_POPULAR)
            mRepository.getPopularMovies(this);
        else if (currentSort == MovieSortType.SORT_BY_TOP_RATED)
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
        if (mDualPane) {
            mOnMovieSelected.onSelected(movie, initialView);
        } else {
            mView.goToMovieActivity(movie);
        }
    }

    @Override
    public void onLoadedMovies(List<Movie> movies, int type) {
        if (type != currentSort)
            return;

        mView.hideLoading();
        mView.showMovies(movies);

        mMovies = movies;

        if (mDualPane) {
            if (movies == null || movies.size()==0)
                return;

            if (initialView) {
                viewMovie(movies.get(0));
                initialView = false;
            }
        }
    }

    @Override
    public void onDataNotAvailable() {
        mView.hideLoading();
        mView.showNoInternet();
    }

    @Override
    public void onPopularSelected() {
        currentSort = MovieSortType.SORT_BY_POPULAR;
        initialView = true;
        mView.writeSortingState(currentSort);
        mView.showLoading();
        mRepository.getPopularMovies(this);
    }

    @Override
    public void onTopRatedSelected() {
        currentSort = MovieSortType.SORT_BY_TOP_RATED;
        initialView = true;
        mView.writeSortingState(currentSort);
        mView.showLoading();
        mRepository.getTopRatedMovies(this);
    }

    @Override
    public void onFavoriteSelected() {
        currentSort = MovieSortType.SORT_BY_FAVORITE;
        initialView = true;
        mView.writeSortingState(currentSort);
        mView.showLoading();
        mRepository.getFavoriteMovies(this);
    }

    @Override
    public void onLoadMore(final int page, int totalItemsCount, final int expectedItemCount) {
        if (currentSort == MovieSortType.SORT_BY_POPULAR)
            mRepository.getPopularResponse(page + 1, new MovieDataSource.LoadPopularResponseCallback() {
                @Override
                public void onLoadPopularResponse(PopularResponse popularResponse) {
                    Log.d(MovieListPresenter.class.getSimpleName(), popularResponse.toString());
                    if (popularResponse.getResults() != null) {
                        for (Movie movie : popularResponse.getResults()) {
                            if (!mMovies.contains(movie)) {
                                mMovies.add(movie);
                            }
                        }
                        mView.showMovies(mMovies);
                    }

                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        else if (currentSort == MovieSortType.SORT_BY_TOP_RATED)
            mRepository.getTopRatedResponse(page + 1, new MovieDataSource.LoadTopRatedResponseCallback() {
                @Override
                public void onLoadTopRatedResponse(TopRatedResponse topRatedResponse) {
                    Log.d(MovieListPresenter.class.getSimpleName(), topRatedResponse.toString());
                    if (topRatedResponse.getResults() != null) {
                        for (Movie movie : topRatedResponse.getResults()) {
                            if (!mMovies.contains(movie)) {
                                mMovies.add(movie);
                            }
                        }
                        mView.showMovies(mMovies);
                    }
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        //else
        //    mRepository.getFavoriteMovies(this);
    }
}
