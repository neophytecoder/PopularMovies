package org.jkarsten.popularmovie.popularmovies.data.source.remote;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.gson.Gson;

import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.util.NetworkUtil;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

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
                        mTopRatedResponse = getTopRateResponseHelper(page);
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

    private TopRatedResponse getTopRateResponseHelper(int page) {
        Uri uri = NetworkUtil.buildPageUri(RemoteMovieDataSource.TOP_RATED_PATH, page);

        TopRatedResponse topRatedResponse = null;
        try {
            Response response = NetworkUtil.makeRequest(mClient, uri);
            String body = response.body().string();
            if (response.isSuccessful()) {
                topRatedResponse = parseTopRatedResponse(body);
                Log.d(RemoteMovieDataSource.class.getSimpleName(), topRatedResponse.toString());
            } else {
                Log.d(RemoteMovieDataSource.class.getSimpleName(), body);
            }

        } catch (IOException exc) {
            Log.d(RemoteMovieDataSource.class.getSimpleName(), exc.toString());
        }

        return topRatedResponse;
    }

    private TopRatedResponse parseTopRatedResponse(String reponse) {
        Gson gson = new Gson();
        return gson.fromJson(reponse, TopRatedResponse.class);
    }

}
