package org.jkarsten.popularmovie.popularmovies.data.source.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.*;
import android.util.Log;

/**
 * Created by juankarsten on 7/13/17.
 */

public class PopularMovieContentProvider extends ContentProvider {
    private SQLiteOpenHelper mDBHelper;

    public static final int MOVIE_TASKS = 100;
    public static final int MOVIE_TASKS_WITH_ID = 101;
    public static final int MOVIE_TASKS_POPULAR = 102;
    public static final int MOVIE_TASKS_TOP_RATED = 103;



    public static final UriMatcher sUriMatcher = buildMatcher();

    public static final UriMatcher buildMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PopularMovieContract.AUTHORITY, PopularMovieContract.PATH_MOVIE, MOVIE_TASKS);
        uriMatcher.addURI(PopularMovieContract.AUTHORITY, PopularMovieContract.PATH_MOVIE + "/" + PopularMovieContract.PATH_POPULAR, MOVIE_TASKS_POPULAR);
        uriMatcher.addURI(PopularMovieContract.AUTHORITY, PopularMovieContract.PATH_MOVIE + "/" + PopularMovieContract.PATH_TOP_RATED, MOVIE_TASKS_TOP_RATED);
        uriMatcher.addURI(PopularMovieContract.AUTHORITY, PopularMovieContract.PATH_MOVIE + "/#", MOVIE_TASKS_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new PopularMovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase database = mDBHelper.getReadableDatabase();

        String limit = uri.getQueryParameter(PopularMovieContract.LIMIT);
        if (limit == null) {
            limit = PopularMovieContract.DEFAULT_LIMIT;
        }

        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_TASKS:
                selection = null;
                selectionArgs = null;
                break;
            case MOVIE_TASKS_WITH_ID:
                String movieId = uri.getLastPathSegment();
                selection = PopularMovieContract.MovieEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{movieId};
                break;
            case MOVIE_TASKS_POPULAR:
                selection = PopularMovieContract.MovieEntry.COLUMN_MOVIE_TYPE + "=?";
                selectionArgs = new String[]{PopularMovieContract.MovieEntry.MOVIE_TYPE_POPULAR+""};
                break;
            case MOVIE_TASKS_TOP_RATED:
                selection = PopularMovieContract.MovieEntry.COLUMN_MOVIE_TYPE + "=?";
                selectionArgs = new String[]{PopularMovieContract.MovieEntry.MOVIE_TYPE_TOP_RATED+""};
                break;
            default:
                throw new UnsupportedOperationException();
        }

        cursor = database.query(PopularMovieContract.MovieEntry.TABLE_NAME, projection,
                selection, selectionArgs, null, null, sortOrder, limit);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = mDBHelper.getWritableDatabase();
        Uri newUri = null;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_TASKS:
                values.remove(PopularMovieContract.MovieEntry._ID);
                long id = database.insert(PopularMovieContract.MovieEntry.TABLE_NAME, null, values);
                if (id >=0 ) {
                    newUri = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(newUri, null);
                }
                break;

            default:
                throw new UnsupportedOperationException();

        }
        database.close();
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = mDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_TASKS:
                try {
                    String columnId = values.getAsString(PopularMovieContract.MovieEntry._ID);
                    values.remove(PopularMovieContract.MovieEntry._ID);
                    int rowAffected = database.update(PopularMovieContract.MovieEntry.TABLE_NAME, values,
                            PopularMovieContract.MovieEntry._ID+"=?", new String[]{columnId+""});
                    if (rowAffected > 0) {
                        Uri newUri = ContentUris.withAppendedId(uri, Long.parseLong(columnId));
                        getContext().getContentResolver().notifyChange(newUri, null);
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                } catch (Exception exc) {
                    Log.d(PopularMovieContentProvider.class.getSimpleName(), exc.toString());
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
        database.close();
        return 0;
    }

}
