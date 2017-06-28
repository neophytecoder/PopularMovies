package org.jkarsten.popularmovie.popularmovies.data.source;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteMovieDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by juankarsten on 6/24/17.
 */

@Module
public class MovieDataModule {
    private Context mContext;
    private LoaderManager mLoaderManager;

    public MovieDataModule(Context context, LoaderManager loaderManager) {
        mContext = context;
        mLoaderManager = loaderManager;
    }

    @Provides
    public MovieDataSource provideMovieDataSource() {
        return new RemoteMovieDataSource();
    }

    @Provides
    public MovieLoader provideMovieLoader(MovieDataSource movieDataSource) {
        return new MovieLoader(movieDataSource, mLoaderManager, mContext);
    }

}
