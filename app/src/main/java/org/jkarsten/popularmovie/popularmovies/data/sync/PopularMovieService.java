package org.jkarsten.popularmovie.popularmovies.data.sync;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by juankarsten on 7/13/17.
 */

public class PopularMovieService extends IntentService {
    public static final String POPULAR_MOVIE_SERVICE_TAG = "popularMovieService";

    public PopularMovieService() {
        super(POPULAR_MOVIE_SERVICE_TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = getBaseContext();
        if(PopularMovieSyncTask.isEmpty(context, PopularMovieSyncTask.ACTION_SYNC_POPULAR_MOVIES)) {
            PopularMovieSyncTask.syncMovies(context, PopularMovieSyncTask.ACTION_SYNC_POPULAR_MOVIES);
        }
        if(PopularMovieSyncTask.isEmpty(context, PopularMovieSyncTask.ACTION_SYNC_TOP_RATED)) {
            PopularMovieSyncTask.syncMovies(context, PopularMovieSyncTask.ACTION_SYNC_TOP_RATED);
        }

    }
}
