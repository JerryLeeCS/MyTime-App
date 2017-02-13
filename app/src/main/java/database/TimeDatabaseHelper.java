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

import item.Frequency;
import item.TaskInfo;
import item.DataModel;

/**
 * Created by jerrylee on 12/26/16.
 */

public class TimeDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = TimeDatabaseHelper.class.getSimpleName();

    private static final String DB_NAME = "MYTIME";
    private static final int DB_VERSION = 1;

    private static final String createFrequencyTable =
            "CREATE TABLE " + Frequency.TABLE + "("
            + Frequency.TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Frequency.TASK_COLUMN + " TEXT,"
            + Frequency.FREQUENCY_COLUMN + " INTEGER);";

    private static final String createTaskInfoTable =
            "CREATE TABLE " + TaskInfo.TABLE + "("
                    + TaskInfo.ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TaskInfo.TASK_COLUMN + " TEXT, "
                    + TaskInfo.TIME_ELAPSED_COLUMN + " INTEGER, "
                    + TaskInfo.START_TIME_COLUMN + " TEXT, "
                    + TaskInfo.END_TIME_COLUMN + " TEXT, "
                    + TaskInfo.DATE_COLUMN + " TEXT);";


    public TimeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.v(TAG,"onInitializing <><><><>");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG,"onCreate<><><><>");

        db.execSQL(createFrequencyTable);
        db.execSQL(createTaskInfoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.v(TAG,"onUpgrade<><><>");

        db.execSQL("DROP TABLE IF EXISTS " + Frequency.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TaskInfo.TABLE);
        onCreate(db);
    }

    public void addFrequency(Frequency frequency){
        Log.v(TAG,"addFrequency...");

        if(getFrequency(frequency) == 0) {
            insertFrequency(frequency);
        }else{
            updateFrequency(frequency, getFrequency(frequency));
        }
    }

    private void insertFrequency(Frequency frequency){
        Log.v(TAG, "insert...");

        ContentValues contentValues = new ContentValues();
        contentValues.put(Frequency.TASK_COLUMN, frequency.getTaskName());
        contentValues.put(Frequency.FREQUENCY_COLUMN, 1);

        getWritableDatabase().insert(Frequency.TABLE,null,contentValues);
        close();
    }

    private void updateFrequency(Frequency frequency, int frequencyOfTask){
        Log.v(TAG, "updateFrequency...");

        ContentValues values = new ContentValues();
        values.put(Frequency.FREQUENCY_COLUMN, frequencyOfTask + 1);

        String selection = Frequency.TASK_COLUMN + " LIKE ?";
        String[] selectionArgs = {frequency.getTaskName()};

        getWritableDatabase().update(
                Frequency.TABLE,
                values,
                selection,
                selectionArgs
        );

        close();
    }


    private int getFrequency(Frequency frequency){
        Log.v(TAG, "getFrequency...");

        String[] projection = {
                Frequency.FREQUENCY_COLUMN
        };

        String selection = Frequency.TASK_COLUMN + " = ?";
        String[] selectionArgs = {frequency.getTaskName()};

        Cursor cursor = getReadableDatabase().query(
                Frequency.TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null
        );

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(Frequency.FREQUENCY_COLUMN));
        }else{
            return 0;
        }
    }

    public List<String> getMostFrequentTaskList(){
        Log.v(TAG,"getMostFrequentTaskList....");
        LinkedList<String> taskList = new LinkedList<>();

        Cursor cursor = null;

        try{
            String[] columns = new String[]{Frequency.TASK_COLUMN};

            cursor = getReadableDatabase().query(
                    Frequency.TABLE,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    Frequency.FREQUENCY_COLUMN + " DESC",
                    "7"
            );

            if(cursor.moveToFirst()){
                do{
                    taskList.add(cursor.getString(cursor.getColumnIndex(Frequency.TASK_COLUMN)));
                }while(cursor.moveToNext());
            }

        }catch (Exception e){
            Log.e(TAG,e.toString());
        }finally {
            return taskList;
        }
    }

    public int insertTaskInfo(TaskInfo taskInfo){
        int majorId;

        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskInfo.ID_COLUMN,taskInfo.getID());
        contentValues.put(TaskInfo.TASK_COLUMN, taskInfo.getTaskName());
        contentValues.put(TaskInfo.TIME_ELAPSED_COLUMN,taskInfo.getElapsedTime());
        contentValues.put(TaskInfo.START_TIME_COLUMN, taskInfo.getStartTime());
        contentValues.put(TaskInfo.END_TIME_COLUMN, taskInfo.getEndTime());
        contentValues.put(TaskInfo.DATE_COLUMN, taskInfo.getDate());

        majorId = (int) getWritableDatabase().insert(TaskInfo.TABLE, null, contentValues);
        close();
        return majorId;
    }

    public List<DataModel> getDataModelListTaskInfo(){
        LinkedList<String> dates = new LinkedList<>();

        Cursor cursor = null;

        ArrayList<DataModel> dataModelList = new ArrayList<DataModel>();
        DataModel dataModel = new DataModel();
        LinkedList<TaskInfo> itemList = new LinkedList<>();

        Log.v(TAG,"on getDataModelList...");

        try{

            String[] columns = new String[]{TaskInfo.ID_COLUMN, TaskInfo.TASK_COLUMN,TaskInfo.TIME_ELAPSED_COLUMN,TaskInfo.DATE_COLUMN,TaskInfo.START_TIME_COLUMN,TaskInfo.END_TIME_COLUMN};
            String where = TaskInfo.DATE_COLUMN + " < ?";
            String[] whereArg = new String[]{"date("+ getLastMondayDate() + ")"};
            String orderBy = TaskInfo.ID_COLUMN + " DESC";
            cursor = getReadableDatabase().query(
                    TaskInfo.TABLE,
                    columns,
                    null ,
                    null ,
                    null, null, orderBy);

            if(cursor.moveToFirst()){
                dates.add(cursor.getString(cursor.getColumnIndex(TaskInfo.DATE_COLUMN)));
                dataModel.setSectionTitle(cursor.getString(cursor.getColumnIndex(TaskInfo.DATE_COLUMN)));
                int totalElapsedTime = 0;
                do{
                    TaskInfo item = new TaskInfo();
                    item.setID(cursor.getString(cursor.getColumnIndex(TaskInfo.ID_COLUMN)));
                    item.setTaskName(cursor.getString(cursor.getColumnIndex(TaskInfo.TASK_COLUMN)));
                    item.setElapsedTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(TaskInfo.TIME_ELAPSED_COLUMN))));
                    item.setStartTime(cursor.getString(cursor.getColumnIndex(TaskInfo.START_TIME_COLUMN)));
                    item.setEndTime(cursor.getString(cursor.getColumnIndex(TaskInfo.END_TIME_COLUMN)));
                    item.setDate(cursor.getString(cursor.getColumnIndex(TaskInfo.DATE_COLUMN)));

                    String date = cursor.getString(cursor.getColumnIndex(TaskInfo.DATE_COLUMN));
                    if(!dates.contains(date)){
                        dates.add(date);
                        dataModel.setTotalTimeElapsed(totalElapsedTime);
                        dataModel.setItemList(itemList);
                        dataModelList.add(dataModel);
                        dataModel = new DataModel();
                        totalElapsedTime = 0;
                        dataModel.setSectionTitle(date);
                        itemList = new LinkedList<>();
                    }
                    totalElapsedTime += cursor.getInt(cursor.getColumnIndex(TaskInfo.TIME_ELAPSED_COLUMN));
                    itemList.add(item);
                }while(cursor.moveToNext());
                close();
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

    public void updateTaskInfo(TaskInfo dataItem){


        Log.v(TAG,"updateContent...");
        ContentValues values = new ContentValues();
        values.put(TaskInfo.TASK_COLUMN, dataItem.getTaskName());
        values.put(TaskInfo.TIME_ELAPSED_COLUMN, dataItem.getElapsedTime());
        values.put(TaskInfo.START_TIME_COLUMN, dataItem.getStartTime());
        values.put(TaskInfo.END_TIME_COLUMN, dataItem.getEndTime());
        values.put(TaskInfo.DATE_COLUMN, dataItem.getDate());

        String where = TaskInfo.ID_COLUMN + " LIKE " + dataItem.getID();

        try {

            Log.v(TAG,String.valueOf(getWritableDatabase().update(TaskInfo.TABLE,values,where,null)));
            getWritableDatabase().update(TaskInfo.TABLE,values,where,null);

        }catch (Exception e){
            Log.v(TAG,e.toString());
        }finally {
            close();
        }
    }

    private String getLastMondayDate(){
        Calendar calendar = Calendar.getInstance();
        int diff = Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK);

        calendar.add(Calendar.DAY_OF_MONTH,diff);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(calendar.getTime());
    }


}
