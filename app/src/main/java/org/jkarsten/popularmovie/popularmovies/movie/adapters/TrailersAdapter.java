package org.jkarsten.popularmovie.popularmovies.movie.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Trailer;
import org.jkarsten.popularmovie.popularmovies.movielist.MovieListAdapter;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juankarsten on 8/15/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    List<Trailer> mTrailers;
    Context mContext;
    OnTrailerClickListener mListener;

    public TrailersAdapter(Context context, OnTrailerClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.mTrailers = trailers;
        this.notifyDataSetChanged();
        Log.d(TrailersAdapter.class.getSimpleName(), "setTrailers " + trailers.size());
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TrailersAdapter.class.getSimpleName(), "onCreateViewHolder");
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Log.d(TrailersAdapter.class.getSimpleName(), "onBindViewHolder");
        if (getItemCount()>position)
            holder.bind(mTrailers.get(position), position);
    }

    @Override
    public int getItemCount() {
        Log.d(TrailersAdapter.class.getSimpleName(), "getItemCount");
        if (mTrailers==null)
            return 0;
        if (mTrailers.size() > 4) {
            return 4;
        }
        return mTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailerTV;
        ImageView playerIV;
        ImageView bottomLineIV;
        View mItemView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            trailerTV = (TextView) itemView.findViewById(R.id.trailer_textview);
            playerIV = (ImageView) itemView.findViewById(R.id.play_imageview);
            bottomLineIV = (ImageView) itemView.findViewById(R.id.bottom_row_line);
        }

        public void bind(final Trailer trailer, int position) {
            if (position == 3) {
                bottomLineIV.setVisibility(View.GONE);
            }
            trailerTV.setText("Trailer " + (position+1));
            mItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemId = getAdapterPosition();
            Trailer trailer = mTrailers.get(itemId);
            mListener.onClick(trailer);
        }
    }

    public interface OnTrailerClickListener {
        void onClick(Trailer trailer);
    }
}
