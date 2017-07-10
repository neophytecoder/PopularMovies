package org.jkarsten.popularmovie.popularmovies.data.source.remote;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.gson.Gson;

import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.util.NetworkUtil;

import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

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
                        mPopularResponse = getPopularResponseHelper(page);
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

    private PopularResponse getPopularResponseHelper(int page) {
        Uri uri = NetworkUtil.buildPageUri(RemoteMovieDataSource.POPULAR_PATH, page);

        PopularResponse popularResponse = null;
        try {
            Response response = NetworkUtil.makeRequest(mClient, uri);
            String body = response.body().string();

            if (response.isSuccessful() ) {
                popularResponse = parsePopularResponse(body);
                Log.d(RemoteMovieDataSource.class.getSimpleName(), popularResponse.toString());
            } else {
                Log.d(RemoteMovieDataSource.class.getSimpleName(), body);
            }

        } catch (MalformedURLException exc) {
            Log.d(RemoteMovieDataSource.class.getSimpleName(), exc.toString());
        } catch (IOException exc) {
            Log.d(RemoteMovieDataSource.class.getSimpleName(), exc.toString());
        }

        return popularResponse;
    }

    private PopularResponse parsePopularResponse(String reponse) {
        Gson gson = new Gson();
        return gson.fromJson(reponse, PopularResponse.class);
    }
}
