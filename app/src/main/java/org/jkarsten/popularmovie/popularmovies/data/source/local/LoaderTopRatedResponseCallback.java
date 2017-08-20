package org.jkarsten.popularmovie.popularmovies.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieDBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juankarsten on 7/16/17.
 */

public class LoaderTopRatedResponseCallback implements LoaderManager.LoaderCallbacks<Cursor>{
    private Context mContext;
    private MovieDataSource.LoadTopRatedResponseCallback mloadTopRatedResponseCallback;
    private int mPage;

    public LoaderTopRatedResponseCallback(Context context) {
        mContext = context;
    }

    public void setLoadTopRatedResponseCallback(MovieDataSource.LoadTopRatedResponseCallback loadTopRatedResponseCallback) {
        this.mloadTopRatedResponseCallback = loadTopRatedResponseCallback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri myUri = PopularMovieContract.CONTENT_URI_MOVIES_TOP_RATED;
        int offset = PopularMovieContract.DEFAULT_LIMIT * mPage + 1;
        myUri = myUri.buildUpon().appendQueryParameter(PopularMovieContract.PAGE,
                offset + "").build();
        return new CursorLoader(mContext, myUri, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        TopRatedResponse response = new TopRatedResponse();
        List<Movie> movies = new ArrayList<>();
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            Movie movie = PopularMovieDBUtils.cursorToMovie(data);
            movies.add(movie);
        }
        response.setResults(movies);
        mloadTopRatedResponseCallback.onLoadTopRatedResponse(response);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setPage(int page) {
        this.mPage = page;
    }
}
