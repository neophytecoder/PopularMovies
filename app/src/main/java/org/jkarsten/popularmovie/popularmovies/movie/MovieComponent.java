package org.jkarsten.popularmovie.popularmovies.movie;

import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;

import dagger.Component;

/**
 * Created by juankarsten on 7/22/17.
 */

@Component(modules = {MovieModule.class, MovieDataModule.class})
public interface MovieComponent {
    void inject(MovieFragment activity);
}
