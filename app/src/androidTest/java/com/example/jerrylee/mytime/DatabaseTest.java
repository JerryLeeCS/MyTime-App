package com.example.jerrylee.mytime;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import database.TimeDatabaseHelper;

/**
 * Created by Jerry on 1/4/2017.
 */

public class DatabaseTest extends AndroidTestCase {

    public void testCreateDatabase(){
        TimeDatabaseHelper helper = new TimeDatabaseHelper(getContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        assertTrue(database.isOpen());
    }
}
