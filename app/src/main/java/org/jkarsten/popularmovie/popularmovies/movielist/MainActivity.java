package org.jkarsten.popularmovie.popularmovies.movielist;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;
import org.jkarsten.popularmovie.popularmovies.movie.MovieActivity;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MovieListContract.View, MovieListAdapter.OnListItemClickListener {
    @Inject MovieListContract.Presenter mPresenter;

    RecyclerView mRecyclerView;
    MovieListAdapter mMovieListAdapter;

    public static final String MOVIE = "movie";

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

        mMovieListAdapter = new MovieListAdapter(this, this);
        mRecyclerView.setAdapter(mMovieListAdapter);
    }

    @Override
    public void setPresenter(MovieListContract.Presenter presenter) {

    }

    @Override
    public void showMovies(List<Movie> movies) {
        mMovieListAdapter.setMovies(movies);
    }

    @Override
    public void onClick(Movie movie) {
        mPresenter.viewMovie(movie);
    }

    @Override
    public void goToMovieActivity(Movie movie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(MOVIE, movie);
        startActivity(intent);
    }
}
