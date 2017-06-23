package org.jkarsten.popularmovie.popularmovies.movielist;

import dagger.Component;

/**
 * Created by juankarsten on 6/23/17.
 */

@Component(modules = {MovieListModule.class})
public interface MovieListComponent {
    public void inject(MainActivity mainActivity);
}
