package org.jkarsten.popularmovie.popularmovies.movielist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jkarsten.popularmovie.popularmovies.R;

public class MainActivity extends AppCompatActivity implements MovieListContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void setPresenter(MovieListContract.Presenter presenter) {

    }
}
