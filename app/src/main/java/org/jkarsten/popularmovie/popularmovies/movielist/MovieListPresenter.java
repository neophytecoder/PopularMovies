package org.jkarsten.popularmovie.popularmovies.movielist;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by juankarsten on 6/23/17.
 */

public class MovieListPresenter implements MovieListContract.Presenter {
    MovieListContract.View mView;

    @Inject
    public MovieListPresenter(MovieListContract.View movieListView) {
        mView = movieListView;
        Log.d(MovieListPresenter.class.getSimpleName(), "created");
    }

    @Override
    public void start() {

    }

    @Override
    public void loadList() {

    }
}
