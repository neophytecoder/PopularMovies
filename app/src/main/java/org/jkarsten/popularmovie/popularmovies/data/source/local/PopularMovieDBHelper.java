package org.jkarsten.popularmovie.popularmovies.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juankarsten on 7/13/17.
 */

public class PopularMovieDBHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DB_NAME = "PopularMovie.db";

    public static final String CREATE_DB_QUERY = "CREATE TABLE " + PopularMovieContract.MovieEntry.TABLE_NAME + "(" +
            PopularMovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PopularMovieContract.MovieEntry.COLUMN_ID + " INTEGER UNIQUE NOT NULL, " +
            PopularMovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            PopularMovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXTNOT NULL, " +
            PopularMovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL," +
            PopularMovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            PopularMovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL, " +
            PopularMovieContract.MovieEntry.COLUMN_VIDEO + " NUMERIC NOT NULL, " +
            PopularMovieContract.MovieEntry.COLUMN_FAVORITE + " NUMERIC NOT NULL, " +
            PopularMovieContract.MovieEntry.COLUMN_MOVIE_TYPE + " INTEGER NOT NULL " +
            ")";
    public static final String DELETE_DB_QUERY = "";

    public PopularMovieDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_DB_QUERY);
        onCreate(db);
    }
}
