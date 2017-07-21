package org.jkarsten.popularmovie.popularmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.jkarsten.popularmovie.popularmovies.data.source.local.PopularMovieContract;
import org.jkarsten.popularmovie.popularmovies.data.source.local.PopularMovieDBHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;


/**
 * Created by juankarsten on 7/15/17.
 */

@RunWith(AndroidJUnit4.class)
public class TestPopularMovieContentProvider {
    /* Context used to access various parts of the system */
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    /**
     * Because we annotate this method with the @Before annotation, this method will be called
     * before every single method with an @Test annotation. We want to start each test clean, so we
     * delete all entries in the tasks directory to do so.
     */
    @Before
    public void setUp() {
        /* Use TaskDbHelper to get access to a writable database */
        PopularMovieDBHelper dbHelper = new PopularMovieDBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(PopularMovieContract.MovieEntry.TABLE_NAME, null, null);
    }

    //================================================================================
    // Test Insert
    //================================================================================


    /**
     * Tests inserting a single row of data via a ContentResolver
     */
    @Test
    public void testInsert() {
        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(PopularMovieContract.MovieEntry.COLUMN_ID, "1");
        testTaskValues.put(PopularMovieContract.MovieEntry.COLUMN_TITLE, "one is anumber");
        testTaskValues.put(PopularMovieContract.MovieEntry.COLUMN_POSTER_PATH, "/32748787392.png");
        testTaskValues.put(PopularMovieContract.MovieEntry.COLUMN_RELEASE_DATE, "290290");
        testTaskValues.put(PopularMovieContract.MovieEntry.COLUMN_OVERVIEW, "gooddddd matin good");
        testTaskValues.put(PopularMovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, "8");
        testTaskValues.put(PopularMovieContract.MovieEntry.COLUMN_VIDEO, "1");
        testTaskValues.put(PopularMovieContract.MovieEntry.COLUMN_FAVORITE, "20.3");
        testTaskValues.put(PopularMovieContract.MovieEntry.COLUMN_MOVIE_TYPE, "1");


        /* TestContentObserver allows us to test if notifyChange was called appropriately */
        TestUtilities.TestContentObserver taskObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (tasks) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                PopularMovieContract.CONTENT_URI_MOVIES,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                taskObserver);


        Uri uri = contentResolver.insert(PopularMovieContract.CONTENT_URI_MOVIES, testTaskValues);


        Uri expectedUri = ContentUris.withAppendedId(PopularMovieContract.CONTENT_URI_MOVIES, 1);

        String insertProviderFailed = "Unable to insert item through Provider";
        assertEquals(insertProviderFailed, uri, expectedUri);

        /*
         * If this fails, it's likely you didn't call notifyChange in your insert method from
         * your ContentProvider.
         */
        taskObserver.waitForNotificationOrFail();

        /*
         * waitForNotificationOrFail is synchronous, so after that call, we are done observing
         * changes to content and should therefore unregister this observer.
         */
        contentResolver.unregisterContentObserver(taskObserver);
    }
}
