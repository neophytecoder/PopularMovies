package org.jkarsten.popularmovie.popularmovies.movie;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.Review;
import org.jkarsten.popularmovie.popularmovies.data.Trailer;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;
import org.jkarsten.popularmovie.popularmovies.databinding.ActivityMovieBinding;
import org.jkarsten.popularmovie.popularmovies.movie.adapters.TrailersAdapter;
import org.jkarsten.popularmovie.popularmovies.movielist.MainActivity;
import org.jkarsten.popularmovie.popularmovies.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MovieActivity extends AppCompatActivity implements MovieContract.View,
        TrailersAdapter.OnTrailerClickListener {
    public static final String MOVIE_KEY = "movie";

    ImageView mMoviePosterImageView;
    ImageView mHeaderImageView;
    Bundle mSavedInstanceState;

    Movie mMovie;

    @Inject
    MovieContract.Presenter mPresenter;

    ActivityMovieBinding mMovieBinding;

    RecyclerView mReviewsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        getAllViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        try {
            DaggerMovieComponent
                    .builder()
                    .movieModule(new MovieModule(this))
                    .movieDataModule(new MovieDataModule(this, getSupportLoaderManager()))
                    .build()
                    .inject(this);
        } catch (Exception exc) {
            Log.d(MovieActivity.class.getSimpleName(), exc.toString());
        }
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void showMovie(Movie movie) {
        mMovie = movie;

        mMovieBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);
        mMovieBinding.setMMovie(new MovieViewModel(mMovie));

        String path = ImageUtil.buildImageUri(movie.getPosterPath(), this);
        Log.d(MovieActivity.class.getSimpleName(), mMovie + " " +path);
        Picasso.with(this).load(path).into(mMoviePosterImageView);
        Picasso.with(this).load(path).into(mHeaderImageView);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        toolbarLayout.setTitle(mMovie.getTitle());
    }


    public void onMarkedAsFavorite(View view) {
        Log.d(MovieActivity.class.getSimpleName(), "toggled favorite");
        mPresenter.onAddToFavorite();
        //mFloatingActionButton.setImageDrawable(getDrawable(R.mipmap.ic_launcher_round));
        //mFloatingActionButton.setImageResource(R.mipmap.ic_launcher_round);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(MovieActivity.class.getSimpleName(), mPresenter+"start");
        mPresenter.start();
    }

    @Override
    protected void onStop() {
        mPresenter.stop();
        super.onStop();
    }

    private void getAllViews() {
        mReviewsRV = (RecyclerView) findViewById(R.id.reviews_recyclerview);
        TrailersAdapter mTrailersAdapter = new TrailersAdapter(this, this);
        mReviewsRV.setAdapter(mTrailersAdapter);
        mReviewsRV.setLayoutManager(new LinearLayoutManager(this));


        mMoviePosterImageView = (ImageView) findViewById(R.id.moviePosterImageVIew);
        mHeaderImageView = (ImageView) findViewById(R.id.image_header);
        Log.d(MovieActivity.class.getSimpleName(), "recycler view");
    }

    @Override
    public Movie getMovie() {
        if (mSavedInstanceState != null && mSavedInstanceState.containsKey(MOVIE_KEY)) {
            mMovie = (Movie) mSavedInstanceState.getSerializable(MOVIE_KEY);
        } else {
            Intent myIntent = getIntent();
            mMovie =  (Movie) myIntent.getSerializableExtra(MainActivity.MOVIE);
        }

        return mMovie;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MOVIE_KEY, mMovie);

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

    @Override
    public void showReviews(List<Review> reviews) {
        Log.d(MovieActivity.class.getSimpleName(), (reviews!=null)?reviews.toString()+" "+reviews.size():"");
    }

    @Override
    public void showTrailers(final List<Trailer> trailers) {
//        Log.d(MovieActivity.class.getSimpleName(),  (trailers!=null)?mTrailersRV.getAdapter()+" "+trailers.size()+" ":"");
//        mTrailersAdapter.setTrailers(trailers);
//        mTrailersAdapter.notifyDataSetChanged();

        if (trailers==null)
            return;
        if (trailers.size()>=1) {
            mMovieBinding.trailerOneTextview.setText("Trailer 1");
            mMovieBinding.trailerOneConstraintlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Trailer trailer = trailers.get(0);
                    String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
                    openWebPage(url);
                }
            });
        } else {
            mMovieBinding.trailerOneConstraintlayout.setVisibility(View.GONE);
        }

        if (trailers.size()>=2) {
            mMovieBinding.trailerTwoTextview.setText("Trailer 2");
            mMovieBinding.trailerTwoConstraintlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Trailer trailer = trailers.get(1);
                    String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
                    openWebPage(url);
                }
            });
        } else {
            mMovieBinding.trailerTwoConstraintlayout.setVisibility(View.GONE);
        }

    }

    private void openWebPage(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onClick(Trailer trailer) {

    }
}

