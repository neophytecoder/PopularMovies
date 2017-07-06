package org.jkarsten.popularmovie.popularmovies.movielist;

import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by juankarsten on 6/23/17.
 */

public class MovieListPresenter implements MovieListContract.Presenter, MovieDataSource.LoadMoviesCallback {
    MovieListContract.View mView;
    MovieDataSource mRepository;

    @Inject
    public MovieListPresenter(MovieListContract.View movieListView, MovieDataSource repository) {
        mView = movieListView;
        Log.d(MovieListPresenter.class.getSimpleName(), "created");

        mRepository = repository;
    }

    @Override
    public void start() {
        mRepository.getPopularMovies(this);
        Log.d(MovieListPresenter.class.getSimpleName(), "started");
    }


    @Override
    public void viewMovie(Movie movie) {
        mView.goToMovieActivity(movie);
    }



    @Override
    public void onLoadedMovies(List<Movie> movies) {
        mView.showMovies(movies);
    }

    @Override
    public void onDataNotAvailable() {
        // TODO: 6/29/17 do something e.g: device offline
    }

    @Override
    public void onPopularSelected() {
        mRepository.getPopularMovies(this);
    }

    @Override
    public void onTopRatedSelected() {
        // TODO: 7/4/17
    }
}
