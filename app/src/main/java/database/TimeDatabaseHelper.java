package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jerrylee on 12/26/16.
 */

public class TimeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MYTIME";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "ALL_TIME";

    TimeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "TASK TEXT,"
                + "TIME INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertContent(String table, String task, int time ){
        ContentValues values = new ContentValues();
        values.put("TASK", task);
        values.put("TIME",time);
        this.getWritableDatabase().insert(table,null,values);
    }
}
