package org.jkarsten.popularmovie.popularmovies.data.source;

import org.jkarsten.popularmovie.popularmovies.data.Review;

import java.util.List;

/**
 * Created by juankarsten on 8/14/17.
 */

public interface ReviewSource {
    interface LoadReviewsCallback {
        void onLoadedReviews(List<Review> reviews);
        void onDataNotAvailable();
    }

    void getReview(int movieId, int page, LoadReviewsCallback callback);
}
