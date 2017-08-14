package org.jkarsten.popularmovie.popularmovies.data.utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.jkarsten.popularmovie.popularmovies.data.TrailerResponse;
import org.jkarsten.popularmovie.popularmovies.util.NetworkUtil;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by juankarsten on 8/14/17.
 */

public class TrailerNetworkUtil {
    public static final TrailerResponse getTrailerResponse(int movieId, int page, OkHttpClient client) {
        Uri uri = NetworkUtil.buildPageUri(page, movieId+"", "videos");
        TrailerResponse trailerResponse = null;
        try {
            Log.d(TrailerNetworkUtil.class.getSimpleName(), uri.toString());
            Response response = NetworkUtil.makeRequest(client, uri);
            if (response != null && response.isSuccessful()) {
                String body = response.body().string();
                trailerResponse = parseTrailer(body);
            } else {
                Log.d(TrailerNetworkUtil.class.getSimpleName(), response.toString());
            }
        } catch (IOException exception) {
            Log.d(TrailerNetworkUtil.class.getSimpleName(), exception.toString());
        } catch (Exception exception) {
            Log.d(TrailerNetworkUtil.class.getSimpleName(), exception.toString());
        }
        return trailerResponse;
    }

    public static final TrailerResponse parseTrailer(String body) {
        Gson gson = new Gson();
        return gson.fromJson(body, TrailerResponse.class);
    }
}
