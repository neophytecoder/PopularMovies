package org.jkarsten.popularmovie.popularmovies.data.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.jkarsten.popularmovie.popularmovies.data.Movie;
import org.jkarsten.popularmovie.popularmovies.data.source.local.PopularMovieContract;

import java.util.Date;
import java.util.List;

/**
 * Created by juankarsten on 7/13/17.
 */

public class PopularMovieDBUtils {
    public static final String ENTRY_TYPE_POPULAR = "popular-entry";
    public static final String ENTRY_TYPE_TOP_RATED = "top-rated-entry";

    public static final List<Movie> queryTopRatedMovie(Context context, int entryLimit, int position) {
        throw new UnsupportedOperationException();
    }

    public static final void insertMovies(Context context, List<Movie> movies, String action) {
        ContentResolver resolver = context.getContentResolver();
        for (Movie movie : movies) {
            // TODO: 7/13/17 check movie exist
            ContentValues values = new ContentValues();

            resolver.insert(PopularMovieContract.CONTENT_URI_MOVIES, values);
        }
    }

    public static final ContentValues moviesToContentValues(Movie movie) {
        ContentValues values = new ContentValues();

        if (movie.getColumnId() != -1)
            values.put(PopularMovieContract.MovieEntry._ID, movie.getColumnId());
        values.put(PopularMovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview().trim());
        values.put(PopularMovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath().trim());
        values.put(PopularMovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate().getTime() / 1000L);
        values.put(PopularMovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle().trim());
        values.put(PopularMovieContract.MovieEntry.COLUMN_VIDEO, true);
        values.put(PopularMovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(PopularMovieContract.MovieEntry.COLUMN_ID, movie.getId());
        values.put(PopularMovieContract.MovieEntry.COLUMN_FAVORITE, movie.getMarkAsFavorite());
        if (movie.getMovieType() != -1) {
            values.put(PopularMovieContract.MovieEntry.COLUMN_MOVIE_TYPE, movie.getMovieType());
        }
        return values;
    }

    public static final Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setOverview(cursor.getString(cursor.getColumnIndex(PopularMovieContract.MovieEntry.COLUMN_OVERVIEW)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(PopularMovieContract.MovieEntry.COLUMN_POSTER_PATH)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(PopularMovieContract.MovieEntry.COLUMN_TITLE)));
        movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(PopularMovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
        movie.setId(cursor.getInt(cursor.getColumnIndex(PopularMovieContract.MovieEntry.COLUMN_ID)));

        long dateInUnixTimeStamp = cursor.getLong(cursor.getColumnIndex(PopularMovieContract.MovieEntry.COLUMN_RELEASE_DATE));
        movie.setReleaseDate(new Date(dateInUnixTimeStamp * 1000L));

        movie.setColumnId(cursor.getLong(cursor.getColumnIndex(PopularMovieContract.MovieEntry._ID)));

        movie.setMarkAsFavorite(cursor.getInt(cursor.getColumnIndex(PopularMovieContract.MovieEntry.COLUMN_FAVORITE))==1);

        return movie;

    }
}
