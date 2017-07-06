package org.jkarsten.popularmovie.popularmovies.movie;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.movielist.MainActivity;
import org.jkarsten.popularmovie.popularmovies.util.ImageUtil;

import java.text.SimpleDateFormat;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {
    Movie mMovie;

    TextView mMovieTitleTextView;
    ImageView mMoviePosterImageView;
    TextView mYearTextView;
    TextView mRatingTextView;
    TextView mSynopsisTextView;

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
        mMovieTitleTextView = (TextView) findViewById(R.id.movieTitleTextView);
        mMoviePosterImageView = (ImageView) findViewById(R.id.moviePosterImageVIew);
        mYearTextView = (TextView) findViewById(R.id.yearTextView);
        mRatingTextView = (TextView) findViewById(R.id.ratingTextView);
        mSynopsisTextView = (TextView) findViewById(R.id.overviewTextView);
    }

    private void setMovieFromIntent() {
        Intent myIntent = getIntent();
        mMovie =  (Movie) myIntent.getSerializableExtra(MainActivity.MOVIE);
    }

    private void fillViews() {
        mMovieTitleTextView.setText(mMovie.getOriginalTitle());
        String path = ImageUtil.buildImageUri(mMovie.getPosterPath(), this);
        Picasso.with(this).load(path).into(mMoviePosterImageView);
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(mMovie.getReleaseDate()));
        mYearTextView.setText(year+"");
        mRatingTextView.setText(mMovie.getVoteAverage()+"/10");
        mSynopsisTextView.setText(mMovie.getOverview());
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
