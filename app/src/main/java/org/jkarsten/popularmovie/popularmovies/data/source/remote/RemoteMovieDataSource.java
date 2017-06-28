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
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by juankarsten on 6/24/17.
 */

public class RemoteMovieDataSource implements MovieDataSource, LoaderManager.LoaderCallbacks<PopularResponse> {
    public static final String BASE_URL = "https://api.themoviedb.org/3";
    public static final String MOVIE_PATH = "movie";
    public static final String POPULAR_PATH = "popular";


    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_PAGE = "page";

    public static final int LOADER_ID = 345;

    private OkHttpClient mClient;

    private int totalResult;
    private int totalPage;
    private PopularResponse mPopularResponse;

    private LoaderManager mLoaderManager;
    private Loader<PopularResponse> mLoader;
    private LoadPopularResponseCallback mCallback;
    private Context mContext;

    public RemoteMovieDataSource(LoaderManager loaderManager, Context context) {
        mClient = new OkHttpClient();
        mLoaderManager = loaderManager;
        mContext = context;
    }



    @Override
    public void getMovies(LoadMoviesCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getPopularResponse(int page, LoadPopularResponseCallback callback) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_PAGE, page);
        mCallback = callback;

        if (mLoader == null) {
            mLoader = mLoaderManager.initLoader(LOADER_ID, bundle, this);
        } else {
            mLoader = mLoaderManager.restartLoader(LOADER_ID, bundle, this);
        }
        mLoader.forceLoad();
    }

    private PopularResponse getPopularResponseHelper(int page) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(POPULAR_PATH)
                .appendQueryParameter(PARAM_API_KEY, APIKey.API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, "en-US")
                .appendQueryParameter(PARAM_PAGE, page+"")
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = mClient.newCall(request).execute();

            if (response.isSuccessful() ) {
                String body = response.body().string();
                PopularResponse popularResponse = parsePopularResponse(body);

                totalPage = popularResponse.getPage();
                totalResult = popularResponse.getTotalResults();

                return popularResponse;
            } else {
                String body = response.body().toString();
                Log.d(RemoteMovieDataSource.class.getSimpleName(), body);
            }

        } catch (MalformedURLException exc) {
            Log.d(RemoteMovieDataSource.class.getSimpleName(), exc.toString());
        } catch (IOException exc) {
            Log.d(RemoteMovieDataSource.class.getSimpleName(), exc.toString());
        }

        return null;
    }

    private PopularResponse parsePopularResponse(String reponse) {
        Gson gson = new Gson();
        return gson.fromJson(reponse, PopularResponse.class);
    }

    @Override
    public int getTotalPages() {
        return totalPage;
    }

    @Override
    public int getTotalResults() {
        return totalResult;
    }

    @Override
    public Loader<PopularResponse> onCreateLoader(int id, Bundle args) {
        final int page = args.getInt(PARAM_PAGE);
        return new AsyncTaskLoader<PopularResponse>(mContext) {
            @Override
            public PopularResponse loadInBackground() {
                PopularResponse popularResponse = mPopularResponse;
                if (mPopularResponse != null) {
                    deliverResult(mPopularResponse);
                } else {
                    popularResponse = getPopularResponseHelper(page);
                }
                return popularResponse;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<PopularResponse> loader, PopularResponse data) {
        if (data != null) {
            mCallback.onLoadPopularResponse(data);
        } else {
            mCallback.onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<PopularResponse> loader) {

    }
}
