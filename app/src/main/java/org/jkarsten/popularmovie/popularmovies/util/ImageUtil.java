package org.jkarsten.popularmovie.popularmovies.util;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.content.res.Configuration;

import org.jkarsten.popularmovie.popularmovies.R;
import org.jkarsten.popularmovie.popularmovies.movielist.MovieListAdapter;

/**
 * Created by juankarsten on 6/30/17.
 */

public class ImageUtil {
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p";

    public static final String MDPI_SIZE = "w185";
    public static final String HDPI_SIZE = "w342";
    public static final String XHDPI_SIZE = "w500";
    public static final String XXHDPI_SIZE = "w780";
    public static final String XXXHDPI_SIZE = "w780";

    public static final int MDPI = 10;
    public static final int HDPI = 15;
    public static final int XHDPI = 20;
    public static final int XXHDPI = 30;
    public static final int XXXHDPI = 40;

    public static String buildImageUri(String size, String imagePath) {
        if (imagePath == null) {
            return null;
        }
        String url = Uri.parse(IMAGE_URL)
                .buildUpon()
                .appendPath(size)
                .appendPath(imagePath.substring(1))
                .build().toString();

        Log.d(MovieListAdapter.class.getSimpleName(), url);
        return url;
    }

    public static String buildImageUri(String imagePath, Context context) {
        return buildImageUri(XHDPI_SIZE, imagePath);
    }

    public static int getColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = context.getResources().getInteger(R.integer.scaling_factor);

        int noOfColumns = (int) (dpWidth / scalingFactor);
        noOfColumns = (noOfColumns < 2)? 2 : noOfColumns;
        return noOfColumns;
    }

    public static int getDensity(Context context) {
        return (int)(context.getResources().getDisplayMetrics().density * 10) ;
    }

}
