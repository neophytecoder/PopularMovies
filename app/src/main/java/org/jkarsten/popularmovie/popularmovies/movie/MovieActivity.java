package org.jkarsten.popularmovie.popularmovies.movie;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.Review;
import org.jkarsten.popularmovie.popularmovies.data.Trailer;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;
import org.jkarsten.popularmovie.popularmovies.databinding.ActivityMovieBinding;
import org.jkarsten.popularmovie.popularmovies.movielist.MainActivity;
import org.jkarsten.popularmovie.popularmovies.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {
    public static final String MOVIE_KEY = "movie";

    ImageView mMoviePosterImageView;
    Bundle mSavedInstanceState;

    Movie movie;

    @Inject
    MovieContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerMovieComponent
                .builder()
                .movieModule(new MovieModule(this))
                .movieDataModule(new MovieDataModule(this, getSupportLoaderManager()))
                .build()
                .inject(this);

        getAllViews();
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void showMovie(Movie movie) {
        String path = ImageUtil.buildImageUri(movie.getPosterPath(), this);
        Picasso.with(this).load(path).into(mMoviePosterImageView);

        ActivityMovieBinding movieBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);
        movieBinding.setMovie(movie);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onStop() {
        mPresenter.stop();
        super.onStop();
    }

    private void getAllViews() {
        mMoviePosterImageView = (ImageView) findViewById(R.id.moviePosterImageVIew);
    }

    @Override
    public Movie getMovie() {
        if (mSavedInstanceState != null && mSavedInstanceState.containsKey(MOVIE_KEY)) {
            movie = (Movie) mSavedInstanceState.getSerializable(MOVIE_KEY);
        } else {
            Intent myIntent = getIntent();
            movie =  (Movie) myIntent.getSerializableExtra(MainActivity.MOVIE);
        }

        return movie;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MOVIE_KEY, movie);

        super.onSaveInstanceState(outState);
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

    public void onMarkAsFavoriteToggle(View view) {
        mPresenter.onAddToFavorite();
    }

    @Override
    public void showReviews(List<Review> reviews) {
        Log.d(MovieActivity.class.getSimpleName(), (reviews!=null)?reviews.toString()+" "+reviews.size():"");
    }

    @Override
    public void showTrailers(List<Trailer> trailers) {
        Log.d(MovieActivity.class.getSimpleName(), (trailers!=null)?trailers.toString()+" "+trailers.size():"");
    }
}

