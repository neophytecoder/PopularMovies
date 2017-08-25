package org.jkarsten.popularmovie.popularmovies.movielist;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.util.ImageUtil;

import java.util.List;

/**
 * Created by juankarsten on 6/29/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {
    private List<Movie> mMovieList;
    private Context mContext;


    private OnListItemClickListener mListener;

    public MovieListAdapter(Context context, OnListItemClickListener listener) {
        mContext = context;
        mListener = listener;
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

    class MovieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mPosterImageView;

        public MovieListViewHolder(View itemView) {
            super(itemView);

            mPosterImageView = (ImageView) itemView.findViewById(R.id.poster_image);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            String path = ImageUtil.buildImageUri(movie.getPosterPath(), mContext);
            if (path != null) {
                Picasso.with(mContext)
                        .load(path)
                        .into(mPosterImageView);
            } else {
                mPosterImageView.setImageResource(R.drawable.scrim);
            }

        }

        @Override
        public void onClick(View v) {
            int itemId = getAdapterPosition();
            Movie movie = mMovieList.get(itemId);
            mListener.onClick(movie);
        }
    }

    interface OnListItemClickListener {
        void onClick(Movie movie);
    }
}
