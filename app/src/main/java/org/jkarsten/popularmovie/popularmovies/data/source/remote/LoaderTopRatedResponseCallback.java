package org.jkarsten.popularmovie.popularmovies.data.source.remote;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieNetworkUtil;

import okhttp3.OkHttpClient;

/**
 * Created by juankarsten on 7/6/17.
 */

public class LoaderTopRatedResponseCallback implements LoaderManager.LoaderCallbacks<TopRatedResponse> {
    private Context mContext;
    private MovieDataSource.LoadTopRatedResponseCallback mLoadTopRatedResponseCallback;
    private OkHttpClient mClient;

    public LoaderTopRatedResponseCallback(Context context, OkHttpClient client) {
        mContext = context;
        mClient = client;
    }

    public void setLoadTopRatedResponseCallback(MovieDataSource.LoadTopRatedResponseCallback loadTopRatedResponseCallback) {
        this.mLoadTopRatedResponseCallback = loadTopRatedResponseCallback;
    }

    @Override
    public Loader<TopRatedResponse> onCreateLoader(int id, Bundle args) {
        final int page = args.getInt(RemoteMovieDataSource.PARAM_PAGE);
        if (id == RemoteMovieDataSource.LOADER_ID_TOP_RATED)
            return new AsyncTaskLoader<TopRatedResponse>(mContext) {
                private TopRatedResponse mTopRatedResponse;

                @Override
                public TopRatedResponse loadInBackground() {
                    if (mTopRatedResponse == null) {
                        mTopRatedResponse = PopularMovieNetworkUtil.getTopRateResponseHelper(page, mClient);
                    }
                    return mTopRatedResponse;
                }
            };
        return null;
    }

    @Override
    public void onLoadFinished(Loader<TopRatedResponse> loader, TopRatedResponse data) {
        if (data != null) {
            mLoadTopRatedResponseCallback.onLoadTopRatedResponse(data);
        } else {
            mLoadTopRatedResponseCallback.onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<TopRatedResponse> loader) {

    }



}
