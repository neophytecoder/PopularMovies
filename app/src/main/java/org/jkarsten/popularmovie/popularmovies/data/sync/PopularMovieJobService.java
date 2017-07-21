package org.jkarsten.popularmovie.popularmovies.data.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by juankarsten on 7/13/17.
 */

public class PopularMovieJobService extends JobService {
    AsyncTask<Void, Void, Void> asyncTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.d(PopularMovieJobService.class.getSimpleName(), "on start job");
        final Context context = getBaseContext();
        asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                jobFinished(job, false);
                return null;
            }
        };
        asyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        return true;
    }
}
