package org.jkarsten.popularmovie.popularmovies.movie;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataSource;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieListRepository;
import org.jkarsten.popularmovie.popularmovies.data.source.ReviewSource;
import org.jkarsten.popularmovie.popularmovies.data.source.TrailerSource;

import javax.inject.Named;

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
    public MovieContract.Presenter provideMoviePresenter(
            MovieContract.View view, @Named(MovieDataModule.REPO) MovieDataSource repo,
            ReviewSource reviewSourceRepo, TrailerSource trailerSourceRepo) {
        return new MoviePresenter(view, repo, reviewSourceRepo, trailerSourceRepo);
    }
}
