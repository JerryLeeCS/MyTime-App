package database.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import database.TimeDatabaseHelper;
import database.data.model.TaskInfo;
import item.DataModel;

/**
 * Created by jerrylee on 2/9/17.
 */

public class TaskInfoRepo {

    private Context context;

    private static final String TAG = TaskInfoRepo.class.getSimpleName();

    public TaskInfoRepo(Context context){
        this.context = context;
    }

    public static String createTable(){
        return "CREATE TABLE " + TaskInfo.TABLE + "("
                + TaskInfo.ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskInfo.TASK_COLUMN + " TEXT, "
                + TaskInfo.TIME_ELAPSED_COLUMN + " INTEGER, "
                + TaskInfo.START_TIME_COLUMN + " TEXT, "
                + TaskInfo.END_TIME_COLUMN + " TEXT, "
                + TaskInfo.DATE_COLUMN + " TEXT);";
    }

    public int insert(TaskInfo taskInfo){
        int majorId;

        TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskInfo.ID_COLUMN,taskInfo.getID());
        contentValues.put(TaskInfo.TASK_COLUMN, taskInfo.getTaskName());
        contentValues.put(TaskInfo.TIME_ELAPSED_COLUMN,taskInfo.getElapsedTime());
        contentValues.put(TaskInfo.START_TIME_COLUMN, taskInfo.getStartTime());
        contentValues.put(TaskInfo.END_TIME_COLUMN, taskInfo.getEndTime());
        contentValues.put(TaskInfo.DATE_COLUMN, taskInfo.getDate());

        majorId = (int) timeDatabaseHelper.getWritableDatabase().insert(TaskInfo.TABLE, null, contentValues);
        timeDatabaseHelper.close();
        return majorId;
    }

    public void delete(){
        TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(context);
        timeDatabaseHelper.getWritableDatabase().delete(TaskInfo.TABLE,null,null);
        timeDatabaseHelper.close();
    }

    public List<DataModel> getDataModelList(){
        LinkedList<String> dates = new LinkedList<>();

        Cursor cursor = null;

        ArrayList<DataModel> dataModelList = new ArrayList<DataModel>();
        DataModel dataModel = new DataModel();
        LinkedList<TaskInfo> itemList = new LinkedList<>();

        Log.v(TAG,"on getDataModelList...");

        try{
            TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(context);

            String[] columns = new String[]{TaskInfo.ID_COLUMN, TaskInfo.TASK_COLUMN,TaskInfo.TIME_ELAPSED_COLUMN,TaskInfo.DATE_COLUMN,TaskInfo.START_TIME_COLUMN,TaskInfo.END_TIME_COLUMN};
            String where = TaskInfo.DATE_COLUMN + " < ?";
            String[] whereArg = new String[]{"date("+ getLastMondayDate() + ")"};
            String orderBy = TaskInfo.ID_COLUMN + " DESC";
            cursor = timeDatabaseHelper.getReadableDatabase().query(TaskInfo.TABLE,
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
                timeDatabaseHelper.close();
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

    public void updateContent(TaskInfo dataItem){
        Log.v(TAG,"updateContent...");
        ContentValues values = new ContentValues();
        values.put(TaskInfo.TASK_COLUMN, dataItem.getTaskName());
        values.put(TaskInfo.TIME_ELAPSED_COLUMN, dataItem.getElapsedTime());
        values.put(TaskInfo.START_TIME_COLUMN, dataItem.getStartTime());
        values.put(TaskInfo.END_TIME_COLUMN, dataItem.getEndTime());
        values.put(TaskInfo.DATE_COLUMN, dataItem.getDate());

        String where = TaskInfo.ID_COLUMN + " LIKE " + dataItem.getID();

        TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(context);

        try {

            Log.v(TAG,String.valueOf(timeDatabaseHelper.getWritableDatabase().update(TaskInfo.TABLE,values,where,null)));
            timeDatabaseHelper.getWritableDatabase().update(TaskInfo.TABLE,values,where,null);

        }catch (Exception e){
            Log.v(TAG,e.toString());
        }finally {
            timeDatabaseHelper.close();
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
