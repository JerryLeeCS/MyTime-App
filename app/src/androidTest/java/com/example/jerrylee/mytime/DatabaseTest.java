package com.example.jerrylee.mytime;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.filters.MediumTest;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.List;

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

    @MediumTest
    public void testGetMostFrequentTaskList(){
        TimeDatabaseHelper helper = new TimeDatabaseHelper(getContext());
        List<String> list = helper.getMostFrequentTaskList();
        assertEquals(list.get(0),"yes");
        assertNotNull(list);
    }
}
