package org.jkarsten.popularmovie.popularmovies.movie;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.databinding.ActivityMovieBinding;
import org.jkarsten.popularmovie.popularmovies.movielist.MainActivity;
import org.jkarsten.popularmovie.popularmovies.util.ImageUtil;

import java.text.SimpleDateFormat;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {
    Movie mMovie;

    ImageView mMoviePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAllViews();
        setMovieFromIntent();
        fillViews();
    }

    private void getAllViews() {
        mMoviePosterImageView = (ImageView) findViewById(R.id.moviePosterImageVIew);
    }

    private void setMovieFromIntent() {
        Intent myIntent = getIntent();
        mMovie =  (Movie) myIntent.getSerializableExtra(MainActivity.MOVIE);
    }

    private void fillViews() {
        String path = ImageUtil.buildImageUri(mMovie.getPosterPath(), this);
        Picasso.with(this).load(path).into(mMoviePosterImageView);

        ActivityMovieBinding movieBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);
        movieBinding.setMovie(mMovie);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(MovieContract.Presenter presenter) {

    }
}
