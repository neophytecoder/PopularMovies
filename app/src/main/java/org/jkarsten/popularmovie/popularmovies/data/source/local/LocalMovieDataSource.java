package org.jkarsten.popularmovie.popularmovies.data.source.local;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieDBUtils;
import org.jkarsten.popularmovie.popularmovies.movielist.MovieListPresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by juankarsten on 7/16/17.
 */

public class LocalMovieDataSource implements MovieDataSource, LoaderManager.LoaderCallbacks<Cursor> {
    private LoaderManager mLoaderManager;
    private Context mContext;
    private Loader<Cursor> popularLoader;
    private Loader<Cursor> topRatedLoader;
    private Loader<Cursor> favoriteLoader;

    private static final int LOADER_POPULAR = 2345;
    private static final int LOADER_TOP_RATED = 2346;
    private static final int LOADER_FAVORITE = 32424323;

    LoaderPopularResponseCallback mLoaderPopularResponseCallback;
    LoaderTopRatedResponseCallback mLoaderTopRatedResponseCallback;


    public LocalMovieDataSource(LoaderManager loaderManager, Context context) {
        mLoaderManager = loaderManager;
        mContext = context;
    }

    @Override
    public void getPopularMovies(LoadMoviesCallback callback) {

    }

    @Override
    public void getTopRatedMovies(LoadMoviesCallback callback) {

    }

    @Override
    public void getPopularResponse(int page, LoadPopularResponseCallback callback) {
        if (mLoaderPopularResponseCallback == null)
            mLoaderPopularResponseCallback = new LoaderPopularResponseCallback(mContext);
        mLoaderPopularResponseCallback.setLoadPopularResponseCallback(callback);
        mLoaderPopularResponseCallback.setPage(page);
        if (popularLoader == null)
            popularLoader = mLoaderManager.initLoader(LOADER_POPULAR, null, mLoaderPopularResponseCallback);
        else
            popularLoader = mLoaderManager.restartLoader(LOADER_POPULAR, null, mLoaderPopularResponseCallback);
    }

    @Override
    public void getTopRatedResponse(int page, LoadTopRatedResponseCallback callback) {
        mLoaderTopRatedResponseCallback = new LoaderTopRatedResponseCallback(mContext);
        mLoaderTopRatedResponseCallback.setLoadTopRatedResponseCallback(callback);
        mLoaderTopRatedResponseCallback.setPage(page);
        if (topRatedLoader == null)
            topRatedLoader = mLoaderManager.initLoader(LOADER_TOP_RATED, null, mLoaderTopRatedResponseCallback);
        else
            topRatedLoader = mLoaderManager.restartLoader(LOADER_TOP_RATED, null, mLoaderTopRatedResponseCallback);
    }

    @Override
    public void getFavoriteMovies(LoadMoviesCallback callback) {
        LoaderFavoriteResponseCallback loaderFavoriteResponseCallback = new LoaderFavoriteResponseCallback(mContext);
        loaderFavoriteResponseCallback.setCallback(callback);
        if (favoriteLoader == null)
            favoriteLoader = mLoaderManager.initLoader(LOADER_FAVORITE, null, loaderFavoriteResponseCallback);
        else
            favoriteLoader = mLoaderManager.restartLoader(LOADER_FAVORITE, null, loaderFavoriteResponseCallback);
    }

    @Override
    public int getTotalPages() {
        return 0;
    }

    @Override
    public int getTotalResults() {
        return 0;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext, PopularMovieContract.CONTENT_URI_MOVIES_POPULAR, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private boolean movieExist(Movie movie) {
        Uri uri = ContentUris.withAppendedId(PopularMovieContract.CONTENT_URI_MOVIES, movie.getId());
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        boolean exist = cursor.getCount() != 0;
        if (exist) {
            cursor.moveToFirst();
            Log.d(LocalMovieDataSource.class.getSimpleName(), cursor.getInt(cursor.getColumnIndex(PopularMovieContract.MovieEntry.COLUMN_ID))+"");
        }
        if (!cursor.isClosed())
            cursor.close();
        return exist;
    }

    @Override
    public void saveMovie(final Movie movie) {
        if (movie.getPosterPath() == null)
            return;
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ContentValues values = PopularMovieDBUtils.moviesToContentValues(movie);
                if (movieExist(movie))
                    mContext.getContentResolver().update(PopularMovieContract.CONTENT_URI_MOVIES, values, null, null);
                else
                    mContext.getContentResolver().insert(PopularMovieContract.CONTENT_URI_MOVIES, values);
                return null;
            }
        };
        asyncTask.execute();
    }

    @Override
    public Observable<Movie> getMovie(final int id) {
        return Observable.create(new ObservableOnSubscribe<Movie>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Movie> e) throws Exception {
                Uri uri = ContentUris.withAppendedId(PopularMovieContract.CONTENT_URI_MOVIES, id);
                Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);

                if (cursor.getCount() != 0 && cursor.moveToFirst()) {
                    Movie movie = PopularMovieDBUtils.cursorToMovie(cursor);
                    e.onNext(movie);
                } else {
                    e.onError(new Exception("not exist movie"));
                }

                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Movie>> createPopularResponseObservable(final int page) {
        return Observable.create(new ObservableOnSubscribe<List<Movie>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<Movie>> emitter) throws Exception {
                getPopularResponse(page + 1, new LoadPopularResponseCallback() {
                        @Override
                        public void onLoadPopularResponse(PopularResponse popularResponse) {
                            emitter.onNext(popularResponse.getResults());
                            //emitter.onComplete();
                        }

                        @Override
                        public void onDataNotAvailable() {
                            emitter.onError(new Exception("Data unavailable"));
                        }
                    });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }
}
