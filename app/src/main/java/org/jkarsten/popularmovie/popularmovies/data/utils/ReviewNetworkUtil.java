package org.jkarsten.popularmovie.popularmovies.data.utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.jkarsten.popularmovie.popularmovies.data.ReviewResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteMovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteReviewSource;
import org.jkarsten.popularmovie.popularmovies.util.NetworkUtil;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by juankarsten on 8/14/17.
 */

public class ReviewNetworkUtil {
    public static final ReviewResponse getReviews(int movieId, int page, OkHttpClient client) {
        Uri uri = NetworkUtil.buildPageUri(page, movieId+"", RemoteReviewSource.PATH_REVIEWS);
        ReviewResponse reviewResponse = null;
        try {
            Response response = NetworkUtil.makeRequest(client, uri);
            if (response.isSuccessful()) {
                String body = response.body().string();
                reviewResponse = parseReviewResponse(body);
            } else {
                Log.d(RemoteMovieDataSource.class.getSimpleName(), response.toString());
            }
        } catch (IOException exception) {

        }
        return reviewResponse;
    }

    private static final ReviewResponse parseReviewResponse(String body) {
        Gson gson = new Gson();
        return gson.fromJson(body, ReviewResponse.class);
    }
}
