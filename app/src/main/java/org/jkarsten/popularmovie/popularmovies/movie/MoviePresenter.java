package org.jkarsten.popularmovie.popularmovies.movie;

import org.jkarsten.popularmovie.popularmovies.data.Movie;

import javax.inject.Inject;

/**
 * Created by juankarsten on 6/29/17.
 */

public class MoviePresenter implements MovieContract.Presenter {
    private Movie mMovie;
    MovieContract.View mView;

    public MoviePresenter(MovieContract.View view) {
        this.mView = view;
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
        // TODO: 7/22/17 save markasfavorite to database
    }


    @Override
    public void onAddToFavorite() {
        boolean markAsFav = !mMovie.getMarkAsFavorite();
        mMovie.setMarkAsFavorite(markAsFav);
    }

}
