package org.jkarsten.popularmovie.popularmovies.movie;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;

import javax.inject.Inject;

/**
 * Created by juankarsten on 6/29/17.
 */

public class MoviePresenter implements MovieContract.Presenter {
    private Movie mMovie;
    MovieContract.View mView;
    MovieDataSource mRepository;


    public MoviePresenter(MovieContract.View view, MovieDataSource repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void start() {
        mMovie = mView.getMovie();
        if (mMovie != null) {
            mView.showMovie(mMovie);
        }
    }

    @Override
    public void stop() {
        mRepository.saveMovie(mMovie);
    }


    @Override
    public void onAddToFavorite() {
        boolean markAsFav = !mMovie.getMarkAsFavorite();
        mMovie.setMarkAsFavorite(markAsFav);
    }

}
