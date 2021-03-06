package org.jkarsten.popularmovie.popularmovies.data.source.local;

import android.net.Uri;
import android.provider.BaseColumns;


import java.util.Date;

/**
 * Created by juankarsten on 7/13/17.
 */

public class PopularMovieContract {
    public static final String AUTHORITY = "org.jkarsten.popularmovie.popularmovies.data.source.local";
    public static final String PATH_MOVIE = "movies";
    public static final String PATH_COLUMN_ID = "columnId";
    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOP_RATED = "topRated";
    public static final String PATH_FAVORITE = "favorite";


    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI_MOVIES = CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .build();
    public static final Uri CONTENT_URI_MOVIES_COLUMN_ID = CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .appendPath(PATH_COLUMN_ID)
            .build();
    public static final Uri CONTENT_URI_MOVIES_POPULAR = CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .appendPath(PATH_POPULAR)
            .build();
    public static final Uri CONTENT_URI_MOVIES_TOP_RATED = CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .appendPath(PATH_TOP_RATED)
            .build();
    public static final Uri CONTENT_URI_MOVIES_FAVORITE = CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .appendPath(PATH_FAVORITE)
            .build();


    public static final String LIMIT = "limit";
    public static final int DEFAULT_LIMIT = 19;
    public static final String PAGE = "page";

    private PopularMovieContract() {
        //no instance
    }

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_FAVORITE = "mark_as_favorite";
        public static final String COLUMN_MOVIE_TYPE = "movie_type";

        public static final int MOVIE_TYPE_POPULAR = 1;
        public static final int MOVIE_TYPE_TOP_RATED = 2;


    }
}
