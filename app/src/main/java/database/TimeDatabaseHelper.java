package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import item.RecyclerViewItem;

/**
 * Created by jerrylee on 12/26/16.
 */

public class TimeDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = TimeDatabaseHelper.class.getSimpleName();

    private static final String DB_NAME = "MYTIME";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "ALL_TIME";

    private static final String taskColumn = "TASK";
    private static final String timeElapsedColumn = "TIME";
    private static final String startTimeColumn = "START_TIME";
    private static final String endTimeColumn = "END_TIME";
    private static final String dateColumn = "DATE";



    public TimeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + taskColumn +" TEXT,"
                + timeElapsedColumn +" INTEGER,"
                + startTimeColumn + " TEXT,"
                + endTimeColumn + " TEXT,"
                + dateColumn + " TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertContent(String task, int time, String startTime, String endTime, String date){
        ContentValues values = new ContentValues();
        values.put(taskColumn, task);
        values.put(timeElapsedColumn,time);
        values.put(startTimeColumn,startTime);
        values.put(endTimeColumn,endTime);
        values.put(dateColumn,date);
        this.getWritableDatabase().insert(TABLE_NAME,null,values);
    }

    public List<RecyclerViewItem> getRecyclerViewItemList(){
        LinkedList<RecyclerViewItem> itemList = new LinkedList<>();

        Cursor cursor = getReadableDatabase().query(TABLE_NAME,
                                                    new String[]{taskColumn, timeElapsedColumn},
                                                    null, null, null, null, null);

        try{
            if(cursor.moveToFirst()){
                do{
                    RecyclerViewItem item = new RecyclerViewItem();
                    item.setTaskName(cursor.getString(cursor.getColumnIndex(taskColumn)));
                    item.setTimeElapsed(cursor.getString(cursor.getColumnIndex(timeElapsedColumn)));

                    itemList.add(item);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            Log.v(TAG,"Failed to getRecyclerViewItemList....");
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }


        return itemList;
    }



}
