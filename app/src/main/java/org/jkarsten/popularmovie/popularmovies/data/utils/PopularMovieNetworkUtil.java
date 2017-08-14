package org.jkarsten.popularmovie.popularmovies.data.utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteMovieDataSource;
import org.jkarsten.popularmovie.popularmovies.util.NetworkUtil;

import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by juankarsten on 7/13/17.
 */

public class PopularMovieNetworkUtil {

    public final static PopularResponse getPopularResponseHelper(int page, OkHttpClient client) {
        Uri uri = NetworkUtil.buildPageUri(page, RemoteMovieDataSource.POPULAR_PATH);

        PopularResponse popularResponse = null;
        try {
            Response response = NetworkUtil.makeRequest(client, uri);
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

    private static PopularResponse parsePopularResponse(String reponse) {
        Gson gson = new Gson();
        return gson.fromJson(reponse, PopularResponse.class);
    }

    public final static TopRatedResponse getTopRateResponseHelper(int page, OkHttpClient mClient) {
        Uri uri = NetworkUtil.buildPageUri(page, RemoteMovieDataSource.TOP_RATED_PATH);

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

    private static TopRatedResponse parseTopRatedResponse(String reponse) {
        Gson gson = new Gson();
        return gson.fromJson(reponse, TopRatedResponse.class);
    }
}
