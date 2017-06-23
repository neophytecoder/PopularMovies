package org.jkarsten.popularmovie.popularmovies.movielist;

import org.jkarsten.popularmovie.popularmovies.BasePresenter;
import org.jkarsten.popularmovie.popularmovies.BaseView;

/**
 * Created by juankarsten on 6/23/17.
 */

public interface MovieListContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void loadList();
    }
}
