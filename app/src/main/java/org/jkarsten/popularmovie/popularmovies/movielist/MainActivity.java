package org.jkarsten.popularmovie.popularmovies.movielist;

import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MovieListContract.View {
    @Inject MovieListContract.Presenter mPresenter;

    RecyclerView mRecyclerView;
    MovieListAdapter mMovieListAdapter;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.movies_recyclerview);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieListAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(mMovieListAdapter);
    }

    @Override
    public void setPresenter(MovieListContract.Presenter presenter) {

    }

    @Override
    public void showMovies(List<Movie> movies) {
        mMovieListAdapter.setMovies(movies);
    }
}
