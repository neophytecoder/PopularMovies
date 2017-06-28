package org.jkarsten.popularmovie.popularmovies.data.source;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieListRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by juankarsten on 6/24/17.
 */

public class MovieLoader implements LoaderManager.LoaderCallbacks<List<Movie>> {
    private LoaderManager mLoaderManager;
    private MovieDataSource mDataSource;
    private Loader<List<Movie>> mLoader;
    private Context mContext;

    public static final int LOADER_INT = 32;

    @Inject
    public MovieLoader(MovieDataSource dataSource, LoaderManager loaderManager, Context context) {
        mLoaderManager = loaderManager;
        mDataSource = dataSource;
        mContext = context;
    }

    public void start() {

        if (mLoader == null) {
            mLoader = mLoaderManager.initLoader(LOADER_INT, null, this);
        } else {
            mLoader = mLoaderManager.restartLoader(LOADER_INT, null, this);
        }
        mLoader.forceLoad();
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(mContext) {
            @Override
            public List<Movie> loadInBackground() {
                Log.d(MovieLoader.class.getSimpleName(), "init loader");
                mDataSource.getMovies();
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
