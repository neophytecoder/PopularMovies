package org.jkarsten.popularmovie.popularmovies.movielist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jkarsten.popularmovie.popularmovies.R;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MovieListContract.View {
    @Inject MovieListContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMovieListComponent.builder()
                .movieListModule(new MovieListModule(this))
                .build().inject(this);
    }

    @Override
    public void setPresenter(MovieListContract.Presenter presenter) {

    }
}
