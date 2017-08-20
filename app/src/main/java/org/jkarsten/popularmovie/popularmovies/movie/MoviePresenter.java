package org.jkarsten.popularmovie.popularmovies.movie;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.Review;
import org.jkarsten.popularmovie.popularmovies.data.Trailer;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.source.ReviewSource;
import org.jkarsten.popularmovie.popularmovies.data.source.TrailerSource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by juankarsten on 6/29/17.
 */

public class MoviePresenter implements MovieContract.Presenter, ReviewSource.LoadReviewsCallback,
        TrailerSource.LoadTrailersCallback {
    private Movie mMovie;
    MovieContract.View mView;
    MovieDataSource mRepository;
    ReviewSource mReviewRepository;
    TrailerSource mTrailerRepository;

    boolean mDualPane;

    public MoviePresenter(MovieContract.View view, MovieDataSource repository,
                          ReviewSource reviewSource, TrailerSource trailerSource) {
        mView = view;
        mRepository = repository;
        mReviewRepository = reviewSource;
        mTrailerRepository = trailerSource;
    }

    @Override
    public void start() {
        mDualPane = mView.isDualPane();

        Movie movie = mView.getMovie();
        if (movie == null && mDualPane) {
            movie = mMovie;
        }

        if (movie != null) {
            mView.showMovie(movie);
            mReviewRepository.getReview(movie.getId(), 1, this);
            mTrailerRepository.getTrailers(movie.getId(), 1, this);

            mMovie = movie;
        }
    }

    @Override
    public void stop() {
        saveMovie(mMovie);
    }


    @Override
    public void onAddToFavorite() {
        boolean markAsFav = !mMovie.getMarkAsFavorite();
        mMovie.setMarkAsFavorite(markAsFav);
        mView.showAddToFavoriteChanged(markAsFav);
    }

    @Override
    public void onLoadedReviews(List<Review> reviews) {
        mView.showReviews(reviews);
    }

    @Override
    public void onLoadedTrailers(List<Trailer> trailers) {
        mView.showTrailers(trailers);
    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void saveMovie(Movie movie) {
        mRepository.saveMovie(movie);
    }

    @Override
    public void onNewMovieSelected(Movie movie, boolean initial) {
        if (!mDualPane)
            return;
        if (!initial) {
            stop();
        }
        mMovie = movie;
        start();
    }
}
