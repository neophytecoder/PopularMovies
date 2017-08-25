package org.jkarsten.popularmovie.popularmovies.movielist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jkarsten.popularmovie.popularmovies.OnMovieSelected;
import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.MovieSortType;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;
import org.jkarsten.popularmovie.popularmovies.data.utils.PopularMovieSyncUtils;
import org.jkarsten.popularmovie.popularmovies.movie.MovieActivity;
import org.jkarsten.popularmovie.popularmovies.ui.EndlessRecyclerViewScrollListener;
import org.jkarsten.popularmovie.popularmovies.util.ImageUtil;

import java.util.List;

import javax.inject.Inject;

public class MainFragment extends Fragment implements MovieListContract.View, MovieListAdapter.OnListItemClickListener {
    @Inject MovieListContract.Presenter mPresenter;

    View mRootView;
    RecyclerView mRecyclerView;
    GridLayoutManager mLayoutManager;
    MovieListAdapter mMovieListAdapter;
    TextView noInternetTextView;
    LinearLayout loadingLayout;
    RecyclerView.OnScrollListener mOnScrollListener;

    public static final String MOVIE = "movie";
    public static final String PREFERENCE_SORT_STATE = "sort state";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);

        noInternetTextView = (TextView) mRootView.findViewById(R.id.no_internet);
        loadingLayout = (LinearLayout) mRootView.findViewById(R.id.loading);

        createRecyclerView();

        DaggerMovieListComponent.builder()
                .movieListModule(new MovieListModule(this))
                .movieDataModule(new MovieDataModule(getContext(), getActivity().getSupportLoaderManager()))
                .build()
                .inject(this);

        PopularMovieSyncUtils.initialize(getContext());

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public boolean isDualPane() {
        boolean dualPane = false;
        try {
            if (getActivity() != null) {
                MainActivity activity = (MainActivity) getActivity();
                dualPane = activity.isDualPane();
            }
        } catch (Exception exception) {
            Log.d(MainFragment.class.getSimpleName(), "single pane");
            dualPane = false;
        } finally {
            return dualPane;
        }
    }

    @Override
    public OnMovieSelected getOnMovieSelected() {
        OnMovieSelected onMovieSelected = null;
        try {
            if (getActivity() != null) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity.isDualPane())
                    onMovieSelected = (OnMovieSelected) activity;
            }
        } catch (Exception exception) {
            Log.d(MainFragment.class.getSimpleName(), "single pane");
        } finally {
            return onMovieSelected;
        }
    }

    private void createRecyclerView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.movies_recyclerview);

        int columns = ImageUtil.getColumns(getContext());
        mLayoutManager = new GridLayoutManager(getContext(), columns);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mMovieListAdapter = new MovieListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mMovieListAdapter);
    }

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
        Log.d(MainFragment.class.getSimpleName(), "onOptionsItemSelected " + itemId);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(MovieListContract.Presenter presenter) {

    }

    @Override
    public void showMovies(List<Movie> movies) {
        noInternetTextView.setVisibility(View.GONE);
        mMovieListAdapter.setMovies(movies);

        if (mOnScrollListener == null) {
            mOnScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    mPresenter.onLoadMore(page, totalItemsCount, getExpectedSize());
                }
            };
            mRecyclerView.clearOnScrollListeners();
            mRecyclerView.addOnScrollListener(mOnScrollListener);
        }
    }

    @Override
    public void onClick(Movie movie) {
        mPresenter.viewMovie(movie);
    }

    @Override
    public void goToMovieActivity(Movie movie) {
        Intent intent = new Intent(getContext(), MovieActivity.class);
        intent.putExtra(MOVIE, movie);
        startActivity(intent);

    }

    @Override
    public int readSortingState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getInt(PREFERENCE_SORT_STATE, MovieSortType.SORT_BY_POPULAR);
    }

    @Override
    public void writeSortingState(int state) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREFERENCE_SORT_STATE, state);
        editor.apply();
    }

    @Override
    public void onStop() {
        mPresenter.stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
