package org.jkarsten.popularmovie.popularmovies.movielist;

import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.OnMovieSelected;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.MovieSortType;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by juankarsten on 6/23/17.
 */

public class MovieListPresenter implements MovieListContract.Presenter, MovieDataSource.LoadMoviesCallback {
    private MovieListContract.View mView;
    private MovieDataSource mRepository;
    private int currentSort;

    private boolean initialView;
    private boolean mDualPane;
    private OnMovieSelected mOnMovieSelected;
    private List<Movie> mMovies;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public MovieListPresenter(MovieListContract.View movieListView, MovieDataSource repository) {
        currentSort = MovieSortType.SORT_BY_POPULAR;
        mView = movieListView;
        Log.d(MovieListPresenter.class.getSimpleName(), "created");
        mRepository = repository;
    }

    @Override
    public void start() {
        mMovies = new ArrayList<>();

        mView.showLoading();
        initialView = true;
        mCompositeDisposable = new CompositeDisposable();

        mDualPane = mView.isDualPane();
        if (mDualPane) {
            mOnMovieSelected = mView.getOnMovieSelected();
        }

        currentSort = mView.readSortingState();
        if (currentSort == MovieSortType.SORT_BY_POPULAR) {
            subscribeToPopularMoviesSource(0);
        } else if (currentSort == MovieSortType.SORT_BY_TOP_RATED) {
            subscribeToTopRatedMoviesSource(0);
        } else {
            mRepository.getFavoriteMovies(this);
        }
        Log.d(MovieListPresenter.class.getSimpleName(), "started");
    }

    public void subscribeToPopularMoviesSource(int page) {
        Disposable disposable = mRepository.createPopularResponseObservable(page)
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) throws Exception {
                        onLoadedMovies(movies, MovieSortType.SORT_BY_POPULAR);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(MovieListPresenter.class.getSimpleName(), "onError" + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(MovieListPresenter.class.getSimpleName(), "onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void subscribeToTopRatedMoviesSource(int page) {
        Disposable disposable = mRepository.createTopRatedResponseObservable(page)
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) throws Exception {
                        onLoadedMovies(movies, MovieSortType.SORT_BY_TOP_RATED);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(MovieListPresenter.class.getSimpleName(), "onError" + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(MovieListPresenter.class.getSimpleName(), "onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void stop() {
        mView.writeSortingState(currentSort);
        if (!mCompositeDisposable.isDisposed())
            mCompositeDisposable.dispose();
    }

    @Override
    public void viewMovie(Movie movie) {
        if (mDualPane) {
            mOnMovieSelected.onSelected(movie, initialView);

        } else {
            mView.goToMovieActivity(movie);
        }
    }

    @Override
    public synchronized void onLoadedMovies(List<Movie> movies, int type) {
        if (type != currentSort)
            return;

        if (movies != null) {
            for (Movie movie : movies) {
                if (!mMovies.contains(movie) && movie.getPosterPath() != null) {
                    Log.d(MovieListPresenter.class.getSimpleName(), movie.toString());
                    mMovies.add(movie);
                } else {
                    mMovies.get(mMovies.indexOf(movie)).setMarkAsFavorite(movie.getMarkAsFavorite());
                }
            }
        }

        mView.hideLoading();
        mView.showMovies(mMovies);


        if (mDualPane) {
            if (movies == null || movies.size()==0)
                return;

            if (initialView) {
                viewMovie(movies.get(0));
                initialView = false;
            }
        }
    }

    @Override
    public void onDataNotAvailable() {
        mView.hideLoading();
        mView.showNoInternet();
    }

    @Override
    public void onPopularSelected() {
        mMovies = new ArrayList<>();
        currentSort = MovieSortType.SORT_BY_POPULAR;
        initialView = true;
        mView.writeSortingState(currentSort);
        mView.showLoading();
        mRepository.getPopularMovies(this);
    }

    @Override
    public void onTopRatedSelected() {
        mMovies = new ArrayList<>();
        currentSort = MovieSortType.SORT_BY_TOP_RATED;
        initialView = true;
        mView.writeSortingState(currentSort);
        mView.showLoading();
        mRepository.getTopRatedMovies(this);
    }

    @Override
    public void onFavoriteSelected() {
        mMovies = new ArrayList<>();
        currentSort = MovieSortType.SORT_BY_FAVORITE;
        initialView = true;
        mView.writeSortingState(currentSort);
        mView.showLoading();
        mRepository.getFavoriteMovies(this);
    }

    @Override
    public void onLoadMore(final int page, int totalItemsCount, final int expectedItemCount) {
        if (currentSort == MovieSortType.SORT_BY_POPULAR) {
            subscribeToPopularMoviesSource(page+1);
        } else if (currentSort == MovieSortType.SORT_BY_TOP_RATED) {
            subscribeToTopRatedMoviesSource(page+1);
        } else {
            mRepository.getFavoriteMovies(this);
        }
    }
}
