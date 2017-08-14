package org.jkarsten.popularmovie.popularmovies.data.source.remote;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import org.jkarsten.popularmovie.popularmovies.data.TrailerResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.TrailerSource;
import org.jkarsten.popularmovie.popularmovies.data.utils.TrailerNetworkUtil;
import org.jkarsten.popularmovie.popularmovies.util.NetworkUtil;

import okhttp3.OkHttpClient;

/**
 * Created by juankarsten on 8/14/17.
 */

public class RemoteTrailerSource implements TrailerSource, LoaderManager.LoaderCallbacks<TrailerResponse> {
    private LoaderManager mLoaderManager;
    private Context mContext;
    private TrailerSource.LoadTrailersCallback mCallback;
    private Loader<TrailerResponse> mTrailerResponseLoader;
    private OkHttpClient mClient;

    public static final int LOADER_TRAILER = 4324322;
    public static final String MOVIE_ID = "MOVIE_ID";

    public RemoteTrailerSource(LoaderManager loaderManager, Context context) {
        mLoaderManager = loaderManager;
        mContext = context;
        mClient = new OkHttpClient();
    }

    @Override
    public Loader<TrailerResponse> onCreateLoader(int id, Bundle args) {
        final int movieId = args.getInt(MOVIE_ID);
        if (id == LOADER_TRAILER)
            return new AsyncTaskLoader<TrailerResponse>(mContext) {
                private TrailerResponse trailerResponse;
                @Override
                public TrailerResponse loadInBackground() {
                    if (trailerResponse == null) {
                        trailerResponse = TrailerNetworkUtil.getTrailerResponse(movieId, 1, mClient);
                    }
                    return trailerResponse;
                }
            };
        else
            return null;
    }

    @Override
    public void onLoadFinished(Loader<TrailerResponse> loader, TrailerResponse data) {
        if (data!=null && data.getResults() != null ) {
            mCallback.onLoadedTrailers(data.getResults());
        } else {
            mCallback.onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<TrailerResponse> loader) {

    }

    @Override
    public void getTrailers(int movieId, int page, LoadTrailersCallback callback) {
        mCallback = callback;
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID, movieId);
        if (mTrailerResponseLoader == null) {
            mTrailerResponseLoader = mLoaderManager.initLoader(LOADER_TRAILER, bundle, this);
        } else {
            mTrailerResponseLoader = mLoaderManager.restartLoader(LOADER_TRAILER, bundle, this);
        }
        mTrailerResponseLoader.forceLoad();
    }
}
