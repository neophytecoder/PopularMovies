package org.jkarsten.popularmovie.popularmovies.movielist;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;

import java.util.List;

/**
 * Created by juankarsten on 6/29/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {
    private List<Movie> mMovieList;
    private Context mContext;

    public static final String IMAGE_URL = "https://image.tmdb.org/t/p";
    public String size = "w500";

    public MovieListAdapter(Context context) {
        mContext = context;
    }

    public void setMovies(List<Movie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_list_item, parent, false);
        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListViewHolder holder, int position) {
        if (position >= getItemCount()) {
            return;
        }
        Movie movie = mMovieList.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) {
            return 0;
        }
        return mMovieList.size();
    }

    public String buildImageUri(String size, String imagePath) {
        String url = Uri.parse(IMAGE_URL)
                .buildUpon()
                .appendPath(size)
                .appendPath(imagePath.substring(1))
                .build().toString();

        Log.d(MovieListAdapter.class.getSimpleName(), url);
        return url;
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {
        ImageView mPosterImageView;

        public MovieListViewHolder(View itemView) {
            super(itemView);

            mPosterImageView = (ImageView) itemView.findViewById(R.id.poster_image);
        }

        public void bind(Movie movie) {
            String path = buildImageUri(size, movie.getPosterPath());
            Picasso.with(mContext)
                    .load(path)
                    .into(mPosterImageView);
        }
    }
}
