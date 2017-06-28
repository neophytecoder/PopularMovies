package org.jkarsten.popularmovie.popularmovies.movielist;

import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by juankarsten on 6/23/17.
 */

@Module
public class MovieListModule {
    private MovieListContract.View movieListView;

    public MovieListModule(MovieListContract.View movieListView) {
        this.movieListView = movieListView;
    }

    @Provides
    MovieListContract.View provideView() {
        return movieListView;
    }

    @Provides
    MovieListContract.Presenter providePresenter(@Named(MovieDataModule.REPO) MovieDataSource repo) {
        return new MovieListPresenter(movieListView, repo);
    }
}
