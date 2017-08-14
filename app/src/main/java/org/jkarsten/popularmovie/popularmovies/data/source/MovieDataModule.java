package org.jkarsten.popularmovie.popularmovies.data.source;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import org.jkarsten.popularmovie.popularmovies.data.source.local.LocalMovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteMovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteReviewSource;
import org.jkarsten.popularmovie.popularmovies.data.source.remote.RemoteTrailerSource;

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
    public static final String LOCAL = "local";
    public static final String REPO = "repository";

    public MovieDataModule(Context context, LoaderManager loaderManager) {
        mContext = context;
        mLoaderManager = loaderManager;
    }

    @Provides @Named(REMOTE)
    public MovieDataSource provideRemoteMovieDataSource() {
        return new RemoteMovieDataSource(mLoaderManager, mContext);
    }

    @Provides @Named(LOCAL)
    public MovieDataSource provideLocalMovieDataSource() {
        return new LocalMovieDataSource(mLoaderManager, mContext);
    }


    @Provides @Named(REPO)
    public MovieDataSource provideMovieRepository(@Named(REMOTE) MovieDataSource remoteMovieDataSource,
                                                  @Named(LOCAL) MovieDataSource localMovieDataSource) {
        return new MovieListRepository(remoteMovieDataSource, localMovieDataSource);
    }

    @Provides
    public ReviewSource provideReviewResource() {
        return new RemoteReviewSource(mLoaderManager, mContext);
    }

    @Provides
    public TrailerSource provideTrailerResource() {
        return new RemoteTrailerSource(mLoaderManager, mContext);
    }
}
