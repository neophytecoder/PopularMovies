package org.jkarsten.popularmovie.popularmovies.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieDBUtils;
import org.jkarsten.popularmovie.popularmovies.movielist.MovieListPresenter;

/**
 * Created by juankarsten on 7/16/17.
 */

public class LocalMovieDataSource implements MovieDataSource, LoaderManager.LoaderCallbacks<Cursor> {
    private LoaderManager mLoaderManager;
    Context mContext;

    private static final int LOADER_POPULAR = 2345;
    private static final int LOADER_TOP_RATED = 2346;
    private static final int LOADER_FAVORITE = 32424323;

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
        LoaderPopularResponseCallback loaderPopularResponseCallback = new LoaderPopularResponseCallback(mContext);
        loaderPopularResponseCallback.setLoadPopularResponseCallback(callback);
        mLoaderManager.initLoader(LOADER_POPULAR, null, loaderPopularResponseCallback);
    }

    @Override
    public void getTopRatedResponse(int page, LoadTopRatedResponseCallback callback) {
        LoaderTopRatedResponseCallback loaderTopRatedResponseCallback = new LoaderTopRatedResponseCallback(mContext);
        loaderTopRatedResponseCallback.setLoadTopRatedResponseCallback(callback);
        mLoaderManager.initLoader(LOADER_TOP_RATED, null, loaderTopRatedResponseCallback);
    }

    @Override
    public void getFavoriteMovies(LoadMoviesCallback callback) {
        LoaderFavoriteResponseCallback loaderFavoriteResponseCallback = new LoaderFavoriteResponseCallback(mContext);
        loaderFavoriteResponseCallback.setCallback(callback);
        mLoaderManager.initLoader(LOADER_FAVORITE, null, loaderFavoriteResponseCallback);
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

    @Override
    public void saveMovie(final Movie movie) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ContentValues values = PopularMovieDBUtils.moviesToContentValues(movie);
                mContext.getContentResolver().update(PopularMovieContract.CONTENT_URI_MOVIES, values, null, null);
                return null;
            }
        };
        asyncTask.execute();
    }
}
