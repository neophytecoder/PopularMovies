package org.jkarsten.popularmovie.popularmovies.movie;

import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.Review;
import org.jkarsten.popularmovie.popularmovies.data.Trailer;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.source.ReviewSource;
import org.jkarsten.popularmovie.popularmovies.data.source.TrailerSource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


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

        if (mDualPane) {

        }

        Movie movie = mView.getMovie();
        if (movie == null && mDualPane) {
            movie = mMovie;
        }

        if (movie != null) {
            showMovieToUI(movie);
            mMovie = movie;
            subscribeToMovie();
        }
    }

    private void subscribeToMovie() {
        mRepository.getMovie(mMovie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Movie>() {
                    @Override
                    public void accept(Movie movie) throws Exception {
                        if (movie.getId() == mMovie.getId()) {
                            mMovie = movie;

                            mView.showMovie(movie);
                            mReviewRepository.getReview(movie.getId(), 1, MoviePresenter.this);
                            mTrailerRepository.getTrailers(movie.getId(), 1, MoviePresenter.this);
                        }
                    }
                });
    }

    private void showMovieToUI(Movie movie) {
        mView.showMovie(movie);
        mReviewRepository.getReview(movie.getId(), 1, this);
        mTrailerRepository.getTrailers(movie.getId(), 1, this);
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
