package org.jkarsten.popularmovie.popularmovies.movie;

import org.jkarsten.popularmovie.popularmovies.BasePresenter;
import org.jkarsten.popularmovie.popularmovies.BaseView;
import org.jkarsten.popularmovie.popularmovies.OnMovieSelected;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.Review;
import org.jkarsten.popularmovie.popularmovies.data.Trailer;

import java.util.List;

/**
 * Created by juankarsten on 6/29/17.
 */

public class MovieContract {
    interface View extends BaseView<Presenter> {
        void showMovie(Movie movie);
        Movie getMovie();
        void showReviews(List<Review> reviews);
        void showTrailers(List<Trailer> trailers);
        void showAddToFavoriteChanged(boolean isMarked);

        boolean isDualPane();
    }

    interface Presenter extends BasePresenter {
        void onAddToFavorite();
        void saveMovie(Movie movie);
        void onNewMovieSelected(Movie movie, boolean initial);
    }
}
