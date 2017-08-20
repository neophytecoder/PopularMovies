package org.jkarsten.popularmovie.popularmovies.movielist;

import org.jkarsten.popularmovie.popularmovies.BasePresenter;
import org.jkarsten.popularmovie.popularmovies.BaseView;
import org.jkarsten.popularmovie.popularmovies.OnMovieSelected;
import org.jkarsten.popularmovie.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by juankarsten on 6/23/17.
 */

public interface MovieListContract {
    interface View extends BaseView<Presenter> {
        void showMovies(List<Movie> movies);
        void goToMovieActivity(Movie movie);
        int readSortingState();
        void writeSortingState(int state);
        void showNoInternet();

        boolean isDualPane();
        OnMovieSelected getOnMovieSelected();

        void showLoading();
        void hideLoading();
    }

    interface Presenter extends BasePresenter {
        void viewMovie(Movie movie);
        void onPopularSelected();
        void onTopRatedSelected();
        void onFavoriteSelected();

    }
}
