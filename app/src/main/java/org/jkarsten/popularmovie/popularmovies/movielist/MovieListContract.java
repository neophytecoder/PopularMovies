package org.jkarsten.popularmovie.popularmovies.movielist;

import org.jkarsten.popularmovie.popularmovies.BasePresenter;
import org.jkarsten.popularmovie.popularmovies.BaseView;
import org.jkarsten.popularmovie.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by juankarsten on 6/23/17.
 */

public interface MovieListContract {
    interface View extends BaseView<Presenter> {
        public void showMovies(List<Movie> movies);
        public void goToMovieActivity(Movie movie);
    }

    interface Presenter extends BasePresenter {
        void viewMovie(Movie movie);
        void onPopularSelected();
        void onTopRatedSelected();
    }
}
