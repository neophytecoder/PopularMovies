package org.jkarsten.popularmovie.popularmovies.movielist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieSyncUtils;
import org.jkarsten.popularmovie.popularmovies.movie.MovieActivity;
import org.jkarsten.popularmovie.popularmovies.util.ImageUtil;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MovieListContract.View, MovieListAdapter.OnListItemClickListener {
    @Inject MovieListContract.Presenter mPresenter;

    RecyclerView mRecyclerView;
    MovieListAdapter mMovieListAdapter;

    TextView noInternetTextView;
    LinearLayout loadingLayout;

    public static final String MOVIE = "movie";

    public static final String PREFERENCE_SORT_STATE = "sort state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noInternetTextView = (TextView) findViewById(R.id.no_internet);
        loadingLayout = (LinearLayout) findViewById(R.id.loading);


        createRecyclerView();

        DaggerMovieListComponent.builder()
                .movieListModule(new MovieListModule(this))
                .movieDataModule(new MovieDataModule(this, getSupportLoaderManager()))
                .build().inject(this);


        PopularMovieSyncUtils.initialize(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    private void createRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.movies_recyclerview);

        int columns = ImageUtil.getColumns(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, columns);

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieListAdapter = new MovieListAdapter(this, this);
        mRecyclerView.setAdapter(mMovieListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.popular:
                mPresenter.onPopularSelected();
                return true;
            case R.id.top_rated:
                mPresenter.onTopRatedSelected();
                return true;
            case R.id.favorite:
                mPresenter.onFavoriteSelected();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(MovieListContract.Presenter presenter) {

    }

    @Override
    public void showMovies(List<Movie> movies) {
        noInternetTextView.setVisibility(View.GONE);
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

    @Override
    public int readSortingState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getInt(PREFERENCE_SORT_STATE, MovieListPresenter.SORT_BY_POPULAR);
    }

    @Override
    public void writeSortingState(int state) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREFERENCE_SORT_STATE, state);
        editor.apply();
    }

    @Override
    protected void onStop() {
        mPresenter.stop();
        super.onStop();
    }

    @Override
    public void showNoInternet() {
        noInternetTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
    }
}
