package org.jkarsten.popularmovie.popularmovies.data.source;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteMovieDataSource;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by juankarsten on 6/24/17.
 */

@Module
public class MovieDataModule {
    private Context mContext;
    private LoaderManager mLoaderManager;

    public static final String REMOTE = "remote";
    public static final String REPO = "repository";

    public MovieDataModule(Context context, LoaderManager loaderManager) {
        mContext = context;
        mLoaderManager = loaderManager;
    }

    @Provides @Named(REMOTE)
    public MovieDataSource provideRemoteMovieDataSource() {
        return new RemoteMovieDataSource(mLoaderManager, mContext);
    }

    @Provides @Named(REPO)
    public MovieDataSource provideMovieRepository(@Named(REMOTE) MovieDataSource remoteMovieDataSource) {
        return new MovieListRepository(remoteMovieDataSource);
    }
}
