package org.jkarsten.popularmovie.popularmovies.movie;

import org.jkarsten.popularmovie.popularmovies.data.Movie;

import dagger.Module;
import dagger.Provides;

/**
 * Created by juankarsten on 7/22/17.
 */

@Module
public class MovieModule {
    private MovieContract.View mView;

    public MovieModule(MovieContract.View view) {
        mView = view;
    }

    @Provides
    public MovieContract.View provideMovieView() {
        return mView;
    }

    @Provides
    public MovieContract.Presenter provideMoviePresenter(MovieContract.View view) {
        return new MoviePresenter(view);
    }
}
