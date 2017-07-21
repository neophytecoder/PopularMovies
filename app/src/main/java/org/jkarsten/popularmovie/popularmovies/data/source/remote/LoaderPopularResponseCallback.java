package org.jkarsten.popularmovie.popularmovies.data.source.remote;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieNetworkUtil;

import okhttp3.OkHttpClient;

/**
 * Created by juankarsten on 7/6/17.
 */

public class LoaderPopularResponseCallback implements LoaderManager.LoaderCallbacks<PopularResponse> {
    private Context mContext;
    private MovieDataSource.LoadPopularResponseCallback mPopularResponseCallback;
    private OkHttpClient mClient;

    public LoaderPopularResponseCallback(Context context, OkHttpClient client) {
        mContext = context;
        mClient = client;
    }

    public void setPopularResponseCallback(MovieDataSource.LoadPopularResponseCallback popularResponseCallback) {
        this.mPopularResponseCallback = popularResponseCallback;
    }

    @Override
    public Loader<PopularResponse> onCreateLoader(int id, Bundle args) {
        final int page = args.getInt(RemoteMovieDataSource.PARAM_PAGE);
        if (id == RemoteMovieDataSource.LOADER_ID_POPULAR)
            return new AsyncTaskLoader<PopularResponse>(mContext) {
                private PopularResponse mPopularResponse;

                @Override
                public PopularResponse loadInBackground() {
                    if (mPopularResponse == null) {
                        mPopularResponse = PopularMovieNetworkUtil.getPopularResponseHelper(page, mClient);
                    }
                    return mPopularResponse;
                }
            };
        return null;
    }

    @Override
    public void onLoadFinished(Loader<PopularResponse> loader, PopularResponse data) {
        if (data != null) {
            mPopularResponseCallback.onLoadPopularResponse(data);
        } else {
            mPopularResponseCallback.onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<PopularResponse> loader) {

    }

}
