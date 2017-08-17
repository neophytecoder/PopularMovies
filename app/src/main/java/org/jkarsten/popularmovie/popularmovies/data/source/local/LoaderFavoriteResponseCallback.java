package org.jkarsten.popularmovie.popularmovies.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieDBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juankarsten on 8/17/17.
 */

public class LoaderFavoriteResponseCallback implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context mContext;
    private MovieDataSource.LoadMoviesCallback callback;

    public LoaderFavoriteResponseCallback(Context context) {
        mContext = context;
    }

    public void setCallback(MovieDataSource.LoadMoviesCallback callback) {
        this.callback = callback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext, PopularMovieContract.CONTENT_URI_MOVIES_FAVORITE, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Movie> movies = new ArrayList<>();
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            Movie movie = PopularMovieDBUtils.cursorToMovie(data);
            movies.add(movie);
        }
        callback.onLoadedMovies(movies);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
