package org.jkarsten.popularmovie.popularmovies.data.source;

import org.jkarsten.popularmovie.popularmovies.data.Trailer;

import java.util.List;

/**
 * Created by juankarsten on 8/14/17.
 */

public interface TrailerSource {
    interface LoadTrailersCallback {
        void onLoadedTrailers(List<Trailer> trailers);
        void onDataNotAvailable();
    }

    void getTrailers(int movieId, int page, LoadTrailersCallback callback);
}
