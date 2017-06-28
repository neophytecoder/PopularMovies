package org.jkarsten.popularmovie.popularmovies.movielist;

import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.data.source.MovieLoader;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteMovieDataSource;

import javax.inject.Inject;

/**
 * Created by juankarsten on 6/23/17.
 */

public class MovieListPresenter implements MovieListContract.Presenter {
    MovieListContract.View mView;
    MovieLoader mMovieLoader;

    @Inject
    public MovieListPresenter(MovieListContract.View movieListView, MovieLoader movieLoader) {
        mView = movieListView;
        Log.d(MovieListPresenter.class.getSimpleName(), "created");

        mMovieLoader = movieLoader;
    }

    @Override
    public void start() {
        mMovieLoader.start();
    }

    @Override
    public void loadList() {

    }
}
