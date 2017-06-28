package org.jkarsten.popularmovie.popularmovies.data.source.remote;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by juankarsten on 6/24/17.
 */

public class RemoteMovieDataSource implements MovieDataSource {
    public static final String BASE_URL = "https://api.themoviedb.org/3";
    public static final String MOVIE_PATH = "movie";
    public static final String POPULAR_PATH = "popular";


    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_LANGUAGE = "language";


    private OkHttpClient mClient;

    public RemoteMovieDataSource() {
        mClient = new OkHttpClient();
    }

    //https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1

    @Override
    public List<Movie> getMovies() {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(POPULAR_PATH)
                .appendQueryParameter(PARAM_API_KEY, APIKey.API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, "en-US")
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = mClient.newCall(request).execute();

            Log.d(RemoteMovieDataSource.class.getSimpleName(), url.toString());

            if (response.isSuccessful() ) {
                String body = response.body().string();
                Log.d(RemoteMovieDataSource.class.getSimpleName(), "body"+body);
                PopularResponse popularResponse = parsePopularResponse(body);
                Log.d(RemoteMovieDataSource.class.getSimpleName(), popularResponse.toString());
                return null;
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
}
