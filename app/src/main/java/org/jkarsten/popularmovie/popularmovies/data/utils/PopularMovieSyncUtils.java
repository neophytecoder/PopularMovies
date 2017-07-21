package org.jkarsten.popularmovie.popularmovies.data.utils;

import android.content.Context;
import android.content.Intent;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import org.jkarsten.popularmovie.popularmovies.data.sync.PopularMovieJobService;
import org.jkarsten.popularmovie.popularmovies.data.sync.PopularMovieService;
import org.jkarsten.popularmovie.popularmovies.data.sync.PopularMovieSyncTask;

import java.util.concurrent.TimeUnit;


/**
 * Created by juankarsten on 7/13/17.
 */

public class PopularMovieSyncUtils {
    private static boolean isInitialized = false;

    public static final String POPULAR_SYNC_TAG = "popular-movie-sync-tag";
    public static final int SYNC_INTERVAL_HOURS = 24;
    public static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    public static final int SYNC_FLEXTIME_SECONDS = 3600; // 1 hour


    private static void schedulePopularMovieSync(Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(driver);

        Job popularMovieJob = jobDispatcher.newJobBuilder()
                .setService(PopularMovieJobService.class)
                .setTag(POPULAR_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS+SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        jobDispatcher.schedule(popularMovieJob);
    }

    public synchronized static void initialize(Context context) {
        if (isInitialized) {
            return;
        }

        // check empty if empty calls
        Intent intent = new Intent(context, PopularMovieService.class);
        context.startService(intent);

        //schedulePopularMovieSync(context);
        //schedulePopularMovieSync(context);

        isInitialized = true;
    }
}
