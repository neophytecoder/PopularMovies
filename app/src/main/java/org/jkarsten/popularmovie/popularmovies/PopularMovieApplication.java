package org.jkarsten.popularmovie.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.jkarsten.popularmovie.popularmovies.data.sync.PopularMovieSyncTask;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieSyncUtils;

/**
 * Created by juankarsten on 6/23/17.
 */

public class PopularMovieApplication extends Application {
    // TODO: 6/23/17 Inject context here

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

    }
}
