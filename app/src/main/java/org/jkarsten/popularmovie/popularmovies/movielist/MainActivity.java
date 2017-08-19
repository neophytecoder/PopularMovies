package org.jkarsten.popularmovie.popularmovies.movielist;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.jkarsten.popularmovie.popularmovies.OnMovieSelected;
import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.movie.MovieFragment;

public class MainActivity extends AppCompatActivity implements OnMovieSelected {
    private boolean mDualPane;
    private OnMovieSelected mOnMovieSelectedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDualPane = (findViewById(R.id.dual_pane_layout) != null);
        if (mDualPane) {
            try {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
                mOnMovieSelectedCallback = (OnMovieSelected) fragment;
            } catch (ClassCastException exception) {
                Log.e(MainActivity.class.getSimpleName(), "Movie fragment should implement OnMovieSelected");
            }
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public boolean isDualPane() {
        return mDualPane;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (mDualPane) {
            MovieFragment fragment = (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
            fragment.onSaveInstanceState(outState);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.master_fragment);
        return mainFragment.onOptionsItemSelected(item);
    }

    @Override
    public void onSelected(Movie movie, boolean initial) {
        if (mDualPane && mOnMovieSelectedCallback != null) {
            mOnMovieSelectedCallback.onSelected(movie, initial);
        }
    }

}
