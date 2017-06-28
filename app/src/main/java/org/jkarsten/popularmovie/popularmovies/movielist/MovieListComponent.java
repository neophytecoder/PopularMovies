package org.jkarsten.popularmovie.popularmovies.movielist;

import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;

import dagger.Component;

/**
 * Created by juankarsten on 6/23/17.
 */

@Component(modules = {MovieListModule.class, MovieDataModule.class})
public interface MovieListComponent {
    public void inject(MainActivity mainActivity);
}
