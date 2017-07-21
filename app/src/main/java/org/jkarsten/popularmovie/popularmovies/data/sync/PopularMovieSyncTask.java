package org.jkarsten.popularmovie.popularmovies.data.sync;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.PopularResponse;
import org.jkarsten.popularmovie.popularmovies.data.TopRatedResponse;
import org.jkarsten.popularmovie.popularmovies.data.source.local.PopularMovieContract;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieDBUtils;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieNetworkUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by juankarsten on 7/13/17.
 */

public class PopularMovieSyncTask {
    public static final String ACTION_SYNC_POPULAR_MOVIES = "popular-movie";
    public static final String ACTION_SYNC_TOP_RATED = "top-rated";

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    public static boolean isEmpty(Context context, String action) {
        Cursor cursor = queryAllMovies(context, action);
        int count = cursor.getCount();
        return count == 0;
    }

    public static Cursor queryAllMovies(Context context, String action) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = null;
        if (action.equals(ACTION_SYNC_POPULAR_MOVIES)) {
            uri = PopularMovieContract.CONTENT_URI_MOVIES_POPULAR;
        } else if (action.equals(ACTION_SYNC_TOP_RATED)) {
            uri = PopularMovieContract.CONTENT_URI_MOVIES_TOP_RATED;
        }
        Cursor cursor = cr.query(uri, null, null, null, null);
        return cursor;
    }

    public static synchronized void syncMovies(Context context, String action) {
        ContentResolver contentResolver = context.getContentResolver();
        List<Movie> movies = new ArrayList<>();
        if (action.equals(ACTION_SYNC_POPULAR_MOVIES)) {
            PopularResponse response = PopularMovieNetworkUtil.getPopularResponseHelper(1, OK_HTTP_CLIENT);
            movies = response.getResults();
        } else if (action.equals(ACTION_SYNC_TOP_RATED)) {
            TopRatedResponse response = PopularMovieNetworkUtil.getTopRateResponseHelper(1, OK_HTTP_CLIENT);
            movies = response.getResults();
        }

        List<ContentValues> insertedMovies = new ArrayList<>();
        for (Movie movie: movies) {
            int movieId = movie.getId();
            Uri queryUri = ContentUris.withAppendedId(PopularMovieContract.CONTENT_URI_MOVIES, movieId);
            Cursor cursor = contentResolver.query(queryUri, null, null, null, null);
            if (cursor.getCount() == 0) {
                ContentValues contentValues = PopularMovieDBUtils.moviesToContentValues(movie);
                if (action.equals(ACTION_SYNC_POPULAR_MOVIES)) {
                    contentValues.put(PopularMovieContract.MovieEntry.COLUMN_MOVIE_TYPE,
                            PopularMovieContract.MovieEntry.MOVIE_TYPE_POPULAR);
                } else if (action.equals(ACTION_SYNC_TOP_RATED)) {
                    contentValues.put(PopularMovieContract.MovieEntry.COLUMN_MOVIE_TYPE,
                            PopularMovieContract.MovieEntry.MOVIE_TYPE_TOP_RATED);
                }
                insertedMovies.add(contentValues);
            }
            cursor.close();
        }

        ContentValues[] contentValues = new ContentValues[insertedMovies.size()];
        contentValues = insertedMovies.toArray(contentValues);
        contentResolver.bulkInsert(PopularMovieContract.CONTENT_URI_MOVIES, contentValues);
    }



}
