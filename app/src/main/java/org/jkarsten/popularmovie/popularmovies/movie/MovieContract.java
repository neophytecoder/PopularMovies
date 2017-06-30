package org.jkarsten.popularmovie.popularmovies.movie;

import org.jkarsten.popularmovie.popularmovies.BasePresenter;
import org.jkarsten.popularmovie.popularmovies.BaseView;
import org.jkarsten.popularmovie.popularmovies.data.Movie;

/**
 * Created by juankarsten on 6/29/17.
 */

public class MovieContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void setMovie(Movie movie);
    }
}
