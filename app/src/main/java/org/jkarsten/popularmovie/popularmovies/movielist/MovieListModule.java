package org.jkarsten.popularmovie.popularmovies.movielist;

import android.support.v4.app.LoaderManager;

import org.jkarsten.popularmovie.popularmovies.data.source.MovieListRepository;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieLoader;

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
    MovieListContract.Presenter providePresenter(MovieLoader loader) {
        return new MovieListPresenter(movieListView, loader);
    }
}
