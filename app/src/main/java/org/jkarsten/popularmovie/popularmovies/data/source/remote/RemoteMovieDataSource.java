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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
    public static final String TOP_RATED_PATH = "top_rated";


    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_PAGE = "page";

    public static final int LOADER_ID_POPULAR = 345;
    public static final int LOADER_ID_TOP_RATED = 346;

    private OkHttpClient mClient;

    private int totalResult;
    private int totalPage;
    private TopRatedResponse mTopRatedResponse;

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
    public void getPopularMovies(LoadMoviesCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getTopRatedMovies(LoadMoviesCallback callback) {

    }

    @Override
    public void getPopularResponse(int page, LoadPopularResponseCallback callback) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_PAGE, page);
        mCallback = callback;

        if (mLoader == null) {
            mLoader = mLoaderManager.initLoader(LOADER_ID_POPULAR, bundle, this);
        } else {
            mLoader = mLoaderManager.restartLoader(LOADER_ID_POPULAR, bundle, this);
        }
        mLoader.forceLoad();
    }

    @Override
    public void getTopRatedResponse(int page, LoadPopularResponseCallback callback) {
        throw new UnsupportedOperationException();
    }

    private TopRatedResponse getTopRateResponse(int page) {
        throw new UnsupportedOperationException();
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
        PopularResponse popularResponse = null;
        try {
            url = new URL(uri.toString());
            Log.d(RemoteMovieDataSource.class.getSimpleName(), uri.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = mClient.newCall(request).execute();

            if (response.isSuccessful() ) {
                String body = response.body().string();
                popularResponse = parsePopularResponse(body);
                Log.d(RemoteMovieDataSource.class.getSimpleName(), popularResponse.toString());
                totalPage = popularResponse.getPage();
                totalResult = popularResponse.getTotalResults();

            } else {
                String body = response.body().toString();
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

    private TopRatedResponse parseTopRatedResponse(String reponse) {
        throw new UnsupportedOperationException();
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
        if (id == LOADER_ID_POPULAR)
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
        else
            throw new UnsupportedOperationException();
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
