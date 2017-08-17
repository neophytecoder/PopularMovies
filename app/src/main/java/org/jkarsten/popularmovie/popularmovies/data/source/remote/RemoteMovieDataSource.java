package org.jkarsten.popularmovie.popularmovies.data.source.remote;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.gson.Gson;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.movielist.MovieListPresenter;
import org.jkarsten.popularmovie.popularmovies.util.NetworkUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by juankarsten on 6/24/17.
 */

public class RemoteMovieDataSource implements MovieDataSource  {
    public static final String BASE_URL = "https://api.themoviedb.org/3";
    public static final String MOVIE_PATH = "movie";
    public static final String POPULAR_PATH = "popular";
    public static final String TOP_RATED_PATH = "top_rated";


    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_PAGE = "page";

    public static final int LOADER_ID_TOP_RATED = 346;
    public static final int LOADER_ID_POPULAR = 345;


    private int totalResult;
    private int totalPage;

    private LoaderManager mLoaderManager;
    private Loader<PopularResponse> mPopularResponseLoader;
    private Loader<TopRatedResponse> mTopRatedResponseLoader;

    private LoaderPopularResponseCallback mLoaderPopularResponseCallback;
    private LoaderTopRatedResponseCallback mLoaderTopRatedResponseCallback;

    public RemoteMovieDataSource(LoaderManager loaderManager, Context context) {
        OkHttpClient mClient = new OkHttpClient();
        mLoaderManager = loaderManager;
        Context mContext = context;

        // TODO: 7/6/17 inject LoaderPopularResponseCallback
        mLoaderPopularResponseCallback = new LoaderPopularResponseCallback(mContext, mClient);
        mLoaderTopRatedResponseCallback = new LoaderTopRatedResponseCallback(mContext, mClient);
    }



    @Override
    public void getPopularMovies(LoadMoviesCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getTopRatedMovies(LoadMoviesCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getPopularResponse(int page, LoadPopularResponseCallback callback) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_PAGE, page);
        mLoaderPopularResponseCallback.setPopularResponseCallback(callback);

        if (mPopularResponseLoader == null) {
            mPopularResponseLoader = mLoaderManager.initLoader(LOADER_ID_POPULAR, bundle, mLoaderPopularResponseCallback);
        } else {
            mPopularResponseLoader = mLoaderManager.restartLoader(LOADER_ID_POPULAR, bundle, mLoaderPopularResponseCallback);
        }
        mPopularResponseLoader.forceLoad();
    }

    @Override
    public void getTopRatedResponse(int page, LoadTopRatedResponseCallback callback) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_PAGE, page);
        mLoaderTopRatedResponseCallback.setLoadTopRatedResponseCallback(callback);
        if (mTopRatedResponseLoader == null) {
            mTopRatedResponseLoader = mLoaderManager.initLoader(LOADER_ID_TOP_RATED, bundle, mLoaderTopRatedResponseCallback);
        } else {
            mTopRatedResponseLoader = mLoaderManager.restartLoader(LOADER_ID_TOP_RATED, bundle, mLoaderTopRatedResponseCallback);
        }
        mTopRatedResponseLoader.forceLoad();
    }

    @Override
    public void getFavoriteMovies(LoadMoviesCallback callback) {
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
    public void saveMovie(Movie movie) {

    }
}
