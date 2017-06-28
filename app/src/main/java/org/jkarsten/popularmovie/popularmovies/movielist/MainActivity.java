package org.jkarsten.popularmovie.popularmovies.movielist;

import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MovieListContract.View {
    @Inject MovieListContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createRecyclerView();

        DaggerMovieListComponent.builder()
                .movieListModule(new MovieListModule(this))
                .movieDataModule(new MovieDataModule(this, getSupportLoaderManager()))
                .build().inject(this);

        mPresenter.start();


    }

    private void createRecyclerView() {

    }

    @Override
    public void setPresenter(MovieListContract.Presenter presenter) {

    }

    @Override
    public void showMoviePosters(String[] posterPaths) {

    }
}
