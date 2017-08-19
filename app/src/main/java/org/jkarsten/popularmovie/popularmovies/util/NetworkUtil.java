package org.jkarsten.popularmovie.popularmovies.util;

import android.net.Uri;
import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.data.source.remote.APIKey;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteMovieDataSource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by juankarsten on 7/6/17.
 */

public class NetworkUtil {
    public static Response makeRequest(OkHttpClient mClient, Uri uri)
            throws IOException {
        URL url = new URL(uri.toString());
        Log.d(NetworkUtil.class.getSimpleName(), uri.toString());
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = mClient.newCall(request).execute();
        return response;
    }

    public static Uri buildPageUri(int page, String... paths) {
        Uri.Builder uriBuilder = Uri.parse(RemoteMovieDataSource.BASE_URL).buildUpon()
                .appendPath(RemoteMovieDataSource.MOVIE_PATH);
        for (String path: paths) {
            uriBuilder = uriBuilder.appendPath(path);
        }
        Uri uri = uriBuilder.appendQueryParameter(RemoteMovieDataSource.PARAM_API_KEY, APIKey.API_KEY)
                .appendQueryParameter(RemoteMovieDataSource.PARAM_LANGUAGE, "en-US")
                .appendQueryParameter(RemoteMovieDataSource.PARAM_PAGE, page+"")
                .build();
        return uri;
    }

    public static Uri buildYoutubeThumbnail(String key) {
        return Uri.parse("http://img.youtube.com/vi/"+key+"/0.jpg");
    }
}
