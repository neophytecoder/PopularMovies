package org.jkarsten.popularmovie.popularmovies.movie;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jkarsten.popularmovie.popularmovies.OnMovieSelected;
import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.Review;
import org.jkarsten.popularmovie.popularmovies.data.Trailer;
import org.jkarsten.popularmovie.popularmovies.data.source.MovieDataModule;
import org.jkarsten.popularmovie.popularmovies.databinding.FragmentMovieBinding;
import org.jkarsten.popularmovie.popularmovies.movie.adapters.ReviewAdapter;
import org.jkarsten.popularmovie.popularmovies.movie.adapters.TrailersAdapter;
import org.jkarsten.popularmovie.popularmovies.movielist.MainActivity;
import org.jkarsten.popularmovie.popularmovies.movielist.MainFragment;
import org.jkarsten.popularmovie.popularmovies.util.ImageUtil;

import java.util.List;

import javax.inject.Inject;

public class MovieFragment extends Fragment implements MovieContract.View,
        TrailersAdapter.OnTrailerClickListener, OnMovieSelected{

    public static final String MOVIE_KEY = "movie";

    Movie mMovie;
    @Inject
    MovieContract.Presenter mPresenter;
    FragmentMovieBinding mMovieBinding;

    Bundle mSavedInstanceState;

    View mRootView;
    RecyclerView mReviewsRV;
    RecyclerView mTrailersRV;
    FloatingActionButton mFloatingActionButton;
    ImageView mMoviePosterImageView;
    ImageView mHeaderImageView;
    TrailersAdapter mTrailersAdapter;
    ReviewAdapter mReviewAdapter;

//    boolean mDualPane = false;
//    boolean mInitial = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMovieBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false);
        mRootView = mMovieBinding.getRoot();


        getAllViews();

        DaggerMovieComponent
                .builder()
                .movieModule(new MovieModule(this))
                .movieDataModule(new MovieDataModule(getContext(), getActivity().getSupportLoaderManager()))
                .build()
                .inject(this);

        mSavedInstanceState = savedInstanceState;

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
            MainActivity mainActivity = (MainActivity) getActivity();
            dualPane = mainActivity.isDualPane();
        } catch (ClassCastException exception) {
            Log.d(MovieFragment.class.getSimpleName(), "single pane");
        }
        return dualPane;
    }

    @Override
    public void onStop() {
        mPresenter.stop();
        super.onStop();
    }

    private void getAllViews() {
        mTrailersRV = (RecyclerView) mRootView.findViewById(R.id.trailers_recyclerview);
        mTrailersAdapter = new TrailersAdapter(getContext(), this);
        mTrailersRV.setAdapter(mTrailersAdapter);
        mTrailersRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        mReviewsRV = (RecyclerView) mRootView.findViewById(R.id.reviews_recyclerview);
        mReviewAdapter = new ReviewAdapter(getContext());
        mReviewsRV.setAdapter(mReviewAdapter);
        mReviewsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        mMoviePosterImageView = (ImageView) mRootView.findViewById(R.id.moviePosterImageVIew);
        mHeaderImageView = (ImageView) mRootView.findViewById(R.id.image_header);
        Log.d(MovieFragment.class.getSimpleName(), "recycler view");

        mFloatingActionButton = (FloatingActionButton) mRootView.findViewById(R.id.toggleButton);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MOVIE_KEY, mMovie);
        super.onSaveInstanceState(outState);
    }



    @Override
    public void showMovie(Movie movie) {
        mMovie = movie;
        mMovieBinding.setMMovie(new MovieViewModel(mMovie));

        String path = ImageUtil.buildImageUri(movie.getPosterPath(), getContext());
        Log.d(MovieFragment.class.getSimpleName(), mMovie + " " +path);
        Picasso.with(getContext()).load(path).into(mMoviePosterImageView);
        Picasso.with(getContext()).load(path).into(mHeaderImageView);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar_layout);
        toolbarLayout.setTitle(mMovie.getTitle());

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMarkedAsFavorite(v);
            }
        });
        showAddToFavoriteChanged(mMovie.getMarkAsFavorite());
    }

    @Override
    public Movie getMovie() {
        Movie movie = null;
        if (mSavedInstanceState != null && mSavedInstanceState.containsKey(MOVIE_KEY)) {
            movie = (Movie) mSavedInstanceState.getSerializable(MOVIE_KEY);
            mSavedInstanceState = null;
        } else if (!isDualPane())  {
            Intent myIntent = getActivity().getIntent();
            movie =  (Movie) myIntent.getSerializableExtra(MainFragment.MOVIE);
        }

        return movie;
    }


    @Override
    public void setPresenter(MovieContract.Presenter presenter) {

    }

    @Override
    public void showReviews(List<Review> reviews) {
        Log.d(MovieFragment.class.getSimpleName(), (reviews!=null)?reviews.toString()+" "+reviews.size():"");
        mReviewAdapter.setReviews(reviews);
    }

    @Override
    public void showTrailers(final List<Trailer> trailers) {
        if (trailers==null)
            return;
        mTrailersAdapter.setTrailers(trailers);

    }

    @Override
    public void onClick(Trailer trailer) {
        String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onMarkedAsFavorite(View view) {
        Log.d(MovieFragment.class.getSimpleName(), "toggled favorite");
        mPresenter.onAddToFavorite();
    }

    @Override
    public void showAddToFavoriteChanged(boolean isMarked) {
        if (isMarked) {
            mFloatingActionButton.setImageResource(R.drawable.pentagon_made_of_stars);
        } else {
            mFloatingActionButton.setImageResource(R.drawable.favourite_star);
        }
    }

    @Override
    public void onSelected(Movie movie, boolean initial) {
        mPresenter.onNewMovieSelected(movie, initial);
    }
}

