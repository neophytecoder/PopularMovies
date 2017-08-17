package org.jkarsten.popularmovie.popularmovies.movie.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.data.Review;

import java.util.List;

/**
 * Created by juankarsten on 8/17/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context mContext;
    private List<Review> reviews;

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if (reviews != null)
            holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        if (reviews == null)
            return 0;
        if (reviews.size()>4)
            return 4;
        return reviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewTextView;
        public ReviewViewHolder(View view) {
            super(view);
            reviewTextView = (TextView) view.findViewById(R.id.review_textview);
        }

        public void bind(Review review) {
            reviewTextView.setText(review.getContent());
        }
    }
}
