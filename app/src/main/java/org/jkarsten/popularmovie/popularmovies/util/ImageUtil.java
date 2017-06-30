package org.jkarsten.popularmovie.popularmovies.util;

import android.net.Uri;
import android.util.Log;

import org.jkarsten.popularmovie.popularmovies.movielist.MovieListAdapter;

/**
 * Created by juankarsten on 6/30/17.
 */

public class ImageUtil {
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p";
    public static final String size = "w500";

    public static String buildImageUri(String size, String imagePath) {
        String url = Uri.parse(IMAGE_URL)
                .buildUpon()
                .appendPath(size)
                .appendPath(imagePath.substring(1))
                .build().toString();

        Log.d(MovieListAdapter.class.getSimpleName(), url);
        return url;
    }

}
