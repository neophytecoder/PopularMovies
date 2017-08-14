package org.jkarsten.popularmovie.popularmovies.data.source.remote;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.ReviewResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.ReviewSource;
import org.jkarsten.popularmovie.popularmovies.data.utils.ReviewNetworkUtil;

import okhttp3.OkHttpClient;

/**
 * Created by juankarsten on 8/14/17.
 */

public class RemoteReviewSource implements ReviewSource, LoaderManager.LoaderCallbacks<ReviewResponse> {
    private LoaderManager mLoaderManager;
    private Loader<ReviewResponse> reviewResponseLoader;
    private Context mContext;
    private OkHttpClient mClient;
    private LoadReviewsCallback mCallback;

    public static final String MOVIE_ID = "movie_id";
    public static final int LOADER_REVIEW = 3202;
    public static final String PATH_REVIEWS = "reviews";

    public RemoteReviewSource(LoaderManager loaderManager, Context context) {
        mLoaderManager = loaderManager;
        mContext = context;

        mClient = new OkHttpClient();
    }


    @Override
    public void getReview(int movieId, int page, LoadReviewsCallback callback) {
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID, movieId);
        mCallback = callback;
        if (reviewResponseLoader == null)
            reviewResponseLoader = mLoaderManager.initLoader(LOADER_REVIEW, bundle, this);
        else
            reviewResponseLoader = mLoaderManager.restartLoader(LOADER_REVIEW, bundle, this);
        reviewResponseLoader.forceLoad();
    }

    @Override
    public Loader<ReviewResponse> onCreateLoader(int id, Bundle args) {
        final int movieId = args.getInt(MOVIE_ID);
        if (id == LOADER_REVIEW )
            return new AsyncTaskLoader<ReviewResponse>(mContext) {
                private ReviewResponse reviewResponse;
                @Override
                public ReviewResponse loadInBackground() {
                    if (reviewResponse == null) {
                        reviewResponse = ReviewNetworkUtil.getReviews(movieId, 1, mClient);
                    }
                    return reviewResponse;
                }
            };
        else
            return null;
    }

    @Override
    public void onLoadFinished(Loader<ReviewResponse> loader, ReviewResponse data) {
        if (data != null && data.getResults() != null) {
            mCallback.onLoadedReviews(data.getResults());
        } else {
            mCallback.onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<ReviewResponse> loader) {

    }
}
