package org.jkarsten.popularmovie.popularmovies.movie;

import dagger.Component;

/**
 * Created by juankarsten on 7/22/17.
 */

@Component(modules = {MovieModule.class})
public interface MovieComponent {
    void inject(MovieActivity activity);
}
