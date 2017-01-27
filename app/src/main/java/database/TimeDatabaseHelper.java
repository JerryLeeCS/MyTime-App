package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import item.DataItem;
import item.DataModel;

/**
 * Created by jerrylee on 12/26/16.
 */

public class TimeDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = TimeDatabaseHelper.class.getSimpleName();

    private static final String DB_NAME = "MYTIME";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "ALL_TIME";

    private static final String idColumn = "_id";
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
                + "( "+ idColumn + " INTEGER PRIMARY KEY AUTOINCREMENT,"
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

    public void insertContent(DataItem insertItem){
        ContentValues values = new ContentValues();
        values.put(taskColumn, insertItem.getTaskName());
        values.put(timeElapsedColumn, insertItem.getElapsedTime());
        values.put(startTimeColumn, insertItem.getStartTime());
        values.put(endTimeColumn, insertItem.getEndTime());
        values.put(dateColumn, insertItem.getDate());
        Log.v(TAG,"date => " + insertItem.getDate() + " startTime => " + insertItem.getStartTime() + " endTime => " + insertItem.getEndTime());
        this.getWritableDatabase().insert(TABLE_NAME,null,values);
    }


    public List<DataModel> getDataModelList(){
        LinkedList<String> dates = new LinkedList<>();

        Cursor cursor = null;

        ArrayList<DataModel> dataModelList = new ArrayList<DataModel>();
        DataModel dataModel = new DataModel();
        LinkedList<DataItem> itemList = new LinkedList<>();

        Log.v(TAG,"on getDataModelList...");

        try{
            String[] columns = new String[]{idColumn, taskColumn,timeElapsedColumn,dateColumn,startTimeColumn,endTimeColumn};
            String where = dateColumn + " < ?";
            String[] whereArg = new String[]{"date("+ getLastMondayDate() + ")"};
            String orderBy = idColumn + " DESC";
            cursor = this.getReadableDatabase().query(TABLE_NAME,
                    columns,
                    where,
                    whereArg,
                    null, null, orderBy);

            if(cursor.moveToFirst()){
                dates.add(cursor.getString(cursor.getColumnIndex(dateColumn)));
                dataModel.setSectionTitle(cursor.getString(cursor.getColumnIndex(dateColumn)));
                do{
                    DataItem item = new DataItem();
                    item.setDatabaseID(cursor.getString(cursor.getColumnIndex(idColumn)));
                    item.setTaskName(cursor.getString(cursor.getColumnIndex(taskColumn)));
                    item.setElapsedTimeString(cursor.getString(cursor.getColumnIndex(timeElapsedColumn)));
                    item.setStartTime(cursor.getString(cursor.getColumnIndex(startTimeColumn)));
                    item.setEndTime(cursor.getString(cursor.getColumnIndex(endTimeColumn)));

                    String date = cursor.getString(cursor.getColumnIndex(dateColumn));

                    if(!dates.contains(date)){
                        dates.add(date);
                        dataModel.setItemList(itemList);
                        dataModelList.add(dataModel);
                        dataModel = new DataModel();
                        dataModel.setSectionTitle(date);
                        itemList = new LinkedList<>();
                    }
                    itemList.add(item);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            Log.v(TAG, "Failed to getDataModelList...");
        }finally {
            dataModel.setItemList(itemList);
            dataModelList.add(dataModel);
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return dataModelList;
    }

    private String getLastMondayDate(){
        Calendar calendar = Calendar.getInstance();
        int diff = Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK);

        calendar.add(Calendar.DAY_OF_MONTH,diff);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(calendar.getTime());
    }



}
