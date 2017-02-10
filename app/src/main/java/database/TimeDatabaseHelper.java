package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import database.data.model.Frequency;
import database.data.model.TaskInfo;
import database.data.repo.FrequencyRepo;
import database.data.repo.TaskInfoRepo;

/**
 * Created by jerrylee on 12/26/16.
 */

public class TimeDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = TimeDatabaseHelper.class.getSimpleName();

    private static final String DB_NAME = "MYTIME";
    private static final int DB_VERSION = 1;

    public TimeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FrequencyRepo.createTable());
        db.execSQL(TaskInfoRepo.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.v(TAG,"onUpgrade...");

        db.execSQL("DROP TABLE IF EXISTS " + Frequency.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TaskInfo.TABLE);
        onCreate(db);
    }
}
