package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import item.Frequency;
import item.TaskColorTag;
import item.TaskInfo;
import item.DataModel;
import item.TotalTime;

/**
 * Created by jerrylee on 12/26/16.
 */

public class TimeDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = TimeDatabaseHelper.class.getSimpleName();

    private static final String DB_NAME = "MYTIME";
    private static final int DB_VERSION = 1;

    private static final String createFrequencyTable =
            "CREATE TABLE " + Frequency.TABLE + "("
            + Frequency.ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
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

    private static final String createTotalTimeTable =
            "CREATE TABLE " + TotalTime.TABLE + " ("
            + TotalTime.ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TotalTime.TASK_COLUMN + " TEXT, "
            + TotalTime.TIME_ELAPSED_COLUMN + " INTEGER, "
            + TotalTime.DATE_COLUMN + " TEXT);";

    private static final String createTaskColorTagTable =
            "CREATE TABLE " + TaskColorTag.TABLE + " ("
            + TaskColorTag.ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TaskColorTag.TAG_COLUMN + " TEXT, "
            + TaskColorTag.FREQUENCY_COLUMN + " INTEGER, "
            + TaskColorTag.COLOR_COLUMN + " INTEGER);";

    public TimeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.v(TAG,"onInitializing <><><><>");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG,"onCreate<><><><>");

        db.execSQL(createFrequencyTable);
        db.execSQL(createTaskInfoTable);
        db.execSQL(createTotalTimeTable);
        db.execSQL(createTaskColorTagTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.v(TAG,"onUpgrade<><><>");

        db.execSQL("DROP TABLE IF EXISTS " + Frequency.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TaskInfo.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TotalTime.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TaskColorTag.TABLE);
        onCreate(db);
    }

    public void addOneFrequency(String taskName){
        Log.v(TAG,"addOneFrequency...");

        int frequencyOfTask = getFrequency(taskName);
        if(doesFrequencyExist(taskName)) {
            updateFrequency(taskName, frequencyOfTask + 1);
            Log.v(TAG,"FREQUENCY EXISTED!!!!!!!!");
        }else{
            insertFrequency(taskName);
            Log.v(TAG,"FREQUENCY DOESN'T EXIST!!!!!");
        }
    }

    private boolean doesFrequencyExist(String taskName) {
        Log.v(TAG,"frequencyExist....");

        String[] projection = {
                Frequency.TASK_COLUMN
        };

        String selection = Frequency.TASK_COLUMN + " = ?";
        String[] selectionArgs = {taskName};

        Cursor cursor = null;

        int frequencyCount;
        try{
            cursor = getReadableDatabase().query(
                    Frequency.TABLE,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    null
            );

            cursor.moveToFirst();
        }catch(Exception e){
            Log.e(TAG,"frequencyExist error: " +  e.toString());
        }finally{
            frequencyCount = cursor.getCount();
            cursor.close();
            close();
        }
        return frequencyCount != 0;
    }

    public void removeOneFrequency(String taskName){
        Log.v(TAG,"removeFrequency...");

        int frequencyOfTask = getFrequency(taskName);
        Log.v(TAG,"frequencyOfTask " + frequencyOfTask);
        if(frequencyOfTask > 0){
            updateFrequency(taskName, frequencyOfTask - 1);
        }
    }

    private void insertFrequency(String taskName){
        Log.v(TAG, "insertFrequency...");

        ContentValues contentValues = new ContentValues();
        contentValues.put(Frequency.TASK_COLUMN, taskName);
        contentValues.put(Frequency.FREQUENCY_COLUMN, 1);

        getWritableDatabase().insert(Frequency.TABLE,null,contentValues);
        close();
    }

    private void updateFrequency(String taskName, int destinatedFrequency){
        Log.v(TAG, "updateFrequency...");

        ContentValues values = new ContentValues();
        values.put(Frequency.FREQUENCY_COLUMN, destinatedFrequency);

        String selection = Frequency.TASK_COLUMN + " LIKE ?";
        String[] selectionArgs = {taskName};

        getWritableDatabase().update(
                Frequency.TABLE,
                values,
                selection,
                selectionArgs
        );

        close();
    }

    private int getFrequency(String taskName){
        Log.v(TAG, "getFrequency...");

        String[] projection = {
                Frequency.FREQUENCY_COLUMN
        };

        String selection = Frequency.TASK_COLUMN + " = ?";
        String[] selectionArgs = {taskName};

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
            int frequencyCount = cursor.getInt(cursor.getColumnIndex(Frequency.FREQUENCY_COLUMN));
            cursor.close();
            close();
            return frequencyCount;
        }else{
            cursor.close();
            close();
            return 0;
        }
    }

    public List<String> getMostFrequentTaskList(){
        Log.v(TAG,"getMostFrequentTaskList....");
        LinkedList<String> taskList = new LinkedList<>();


        try{
            String[] columns = new String[]{Frequency.TASK_COLUMN};
            String selection = Frequency.FREQUENCY_COLUMN + " > 0";

            Cursor cursor = getReadableDatabase().query(
                    Frequency.TABLE,
                    columns,
                    selection,
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
            cursor.close();
            close();
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
        return taskList;
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

    public List<DataModel> getDataModelListTaskInfo(){//***
        Log.v(TAG,"on getDataModelList...");
        Stack<String> dates = new Stack<>();

        Cursor cursor = null;

        ArrayList<DataModel> dataModelList = new ArrayList<>();
        DataModel dataModel = new DataModel();
        LinkedList<TaskInfo> itemList = new LinkedList<>();
        int totalElapsedTime = 0;

        try{
            cursor = getCursorDataModelList();

            if(cursor.moveToFirst()){
                dates.add(cursor.getString(cursor.getColumnIndex(TaskInfo.DATE_COLUMN)));
                dataModel.setSectionTitle(cursor.getString(cursor.getColumnIndex(TaskInfo.DATE_COLUMN)));

                do{
                    TaskInfo item = getCursorTaskInfo(cursor);
                    String date = cursor.getString(cursor.getColumnIndex(TaskInfo.DATE_COLUMN));
                    int elapsedTime = cursor.getInt(cursor.getColumnIndex(TaskInfo.TIME_ELAPSED_COLUMN));
                    Log.v(TAG,"Read elapsed time: " + elapsedTime + " Date: " + date);
                    if(!dates.contains(date)){

                        dataModel.setTotalTimeElapsed(totalElapsedTime);
                        dataModel.setItemList(itemList);
                        dataModelList.add(dataModel);
                        Log.v(TAG,"saved totalElapsedTime: " + totalElapsedTime + " date: " + dates.peek());
                        dates.add(date);

                        dataModel = new DataModel();
                        totalElapsedTime = 0;
                        dataModel.setSectionTitle(date);
                        itemList = new LinkedList<>();
                    }
                    totalElapsedTime += elapsedTime;
                    Log.v(TAG,"totalElapsedTime: " + totalElapsedTime);
                    itemList.add(item);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            Log.v(TAG, "Failed to getDataModelList...");
        }finally {
            dataModel.setItemList(itemList);
            dataModel.setTotalTimeElapsed(totalElapsedTime);

            /*
            if(dates.size() < 2){
                dataModel.setTotalTimeElapsed(totalElapsedTime);
            }
            */

            dataModelList.add(dataModel);
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            close();
        }
        return dataModelList;
    }

    private TaskInfo getCursorTaskInfo(Cursor cursor){
        Log.v(TAG, "getCursorTaskInfo...");
        TaskInfo taskInfo = new TaskInfo();

        taskInfo.setID(cursor.getString(cursor.getColumnIndex(TaskInfo.ID_COLUMN)));
        taskInfo.setTaskName(cursor.getString(cursor.getColumnIndex(TaskInfo.TASK_COLUMN)));
        taskInfo.setElapsedTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(TaskInfo.TIME_ELAPSED_COLUMN))));
        taskInfo.setStartTime(cursor.getString(cursor.getColumnIndex(TaskInfo.START_TIME_COLUMN)));
        taskInfo.setEndTime(cursor.getString(cursor.getColumnIndex(TaskInfo.END_TIME_COLUMN)));
        taskInfo.setDate(cursor.getString(cursor.getColumnIndex(TaskInfo.DATE_COLUMN)));

        return taskInfo;
    }

    private Cursor getCursorDataModelList(){
        Log.v(TAG,"getCursorDataModelList...");

        String[] columns = new String[]{TaskInfo.ID_COLUMN, TaskInfo.TASK_COLUMN,TaskInfo.TIME_ELAPSED_COLUMN,TaskInfo.DATE_COLUMN,TaskInfo.START_TIME_COLUMN,TaskInfo.END_TIME_COLUMN};

        String orderBy = TaskInfo.ID_COLUMN + " DESC";
        Cursor cursor = getReadableDatabase().query(
                TaskInfo.TABLE,
                columns,
                /*where*/null,
                /*whereArg*/null,
                null,
                null,
                orderBy);
        return cursor;
    }

    public void updateTaskInfo(TaskInfo dataItem){
        Log.v(TAG,"updateTaskInfo...");
        ContentValues values = setContentValues(dataItem);

        String where = TaskInfo.ID_COLUMN + " LIKE " + dataItem.getID();
        try {
            Log.v(TAG,String.valueOf(getWritableDatabase().update(TaskInfo.TABLE,values,where,null)));
            getWritableDatabase().update(TaskInfo.TABLE,values,where,null);
        }catch (Exception e){
            Log.v(TAG,"updateTaskInfo error: " + e.toString());
        }finally {
            close();
        }
    }

    private ContentValues setContentValues(TaskInfo dataItem){
        ContentValues values = new ContentValues();
        values.put(TaskInfo.TASK_COLUMN, dataItem.getTaskName());
        values.put(TaskInfo.TIME_ELAPSED_COLUMN, dataItem.getElapsedTime());
        values.put(TaskInfo.START_TIME_COLUMN, dataItem.getStartTime());
        values.put(TaskInfo.END_TIME_COLUMN, dataItem.getEndTime());
        values.put(TaskInfo.DATE_COLUMN, dataItem.getDate());

        return values;
    }

    public void addTotalTime(TotalTime totalTime){
        Log.v(TAG,"addTotalTime...");

        int totalTimeOfTask = getTotalTime(totalTime);
        if(totalTimeOfTask == 0){
            insertTotalTime(totalTime);
        }else {
            updateTotalTime(totalTime,totalTimeOfTask + totalTime.getElapsedTime());
        }
    }

    public void removeTotalTime(TotalTime totalTime){
        Log.v(TAG,"removeTotalTime....");
        int totalTimeOfTask = getTotalTime(totalTime);
        if(totalTimeOfTask - totalTime.getElapsedTime() > 1){
            updateTotalTime(totalTime, totalTimeOfTask - totalTime.getElapsedTime());
        }else{
            deleteTotalTime(totalTime);
        }
    }

    private void deleteTotalTime(TotalTime totalTime) {
        Log.v(TAG,"deleteTotalTime...");
        SQLiteDatabase database = null;
        try {
            database = this.getWritableDatabase();

            String selection = TotalTime.TASK_COLUMN + " LIKE ? ";
            String[] selectionArgs = {totalTime.getTask()};

            database.delete(TotalTime.TABLE,selection,selectionArgs);

        }catch (Exception e){
            Log.e(TAG,"deleteTotalTime Error: " + e.toString());
        }finally {
            close();
            Log.v(TAG,"deletedTotalTime ");
        }
    }

    public List<PieEntry> getTotalTimePieDataList(){
        Log.v(TAG, "getTotalTimePieDataList...");
        LinkedList<PieEntry> pieDataList = new LinkedList<>();

        Cursor cursor = null;

        try{
            String[] columns = new String[]{TotalTime.TASK_COLUMN, TotalTime.TIME_ELAPSED_COLUMN};
            String where = TotalTime.DATE_COLUMN  + " = ? ";
            String[] whereArg = new String[]{getPresentDate()};

            cursor = getReadableDatabase().query(
                    TotalTime.TABLE,
                    columns,
                    where,
                    whereArg,
                    null,
                    null,
                    null
            );

            if(cursor.moveToFirst()){
                do{
                    long timeElapsed = cursor.getLong(cursor.getColumnIndex(TotalTime.TIME_ELAPSED_COLUMN));
                    String taskName = cursor.getString(cursor.getColumnIndex(TotalTime.TASK_COLUMN));
                    if(timeElapsed > 1) {
                        PieEntry pieData = new PieEntry(timeElapsed, taskName);
                        pieDataList.add(pieData);
                    }
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e(TAG, "getTotalTimePieDataList error: " + e.toString());
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            close();
        }
        return  pieDataList;
    }

    public BarData getBarChartBarData(){
        Log.v(TAG,"getBarChartBarData...");
        ArrayList<BarEntry> entries = new ArrayList<>();

        Cursor cursor = null;

        Stack<String> stack = new Stack<>();

        long totalTime = 0;

        float i = (float) 0.5;

        try{
            String[] columns = new String[]{TotalTime.TIME_ELAPSED_COLUMN, TotalTime.DATE_COLUMN};
            String where = TotalTime.DATE_COLUMN + " > ?";
            String[] whereArg = new String[]{getLastMondayDate()};

            cursor = getReadableDatabase().query(
                    TotalTime.TABLE,
                    columns,
                    where,
                    whereArg,
                    null,
                    null,
                    null
            );

            if(cursor.moveToFirst()){

                    stack.add(cursor.getString(cursor.getColumnIndex(TotalTime.DATE_COLUMN)));
                do{
                    long timeElapsed = cursor.getLong(cursor.getColumnIndex(TotalTime.TIME_ELAPSED_COLUMN));
                    String date = cursor.getString(cursor.getColumnIndex(TotalTime.DATE_COLUMN));

                    Log.v(TAG,"OOOOOOOOOOO timeElapsed: " + timeElapsed + " date: " + date);

                    if(!stack.contains(date)){
                        BarEntry barEntry = new BarEntry(getDay(stack.peek()), totalTime);
                        entries.add(barEntry);

                        Log.v(TAG,"XXXXXXXXXXXXXX totalTime added is " + totalTime + " day is: " + getDay(stack.peek()) + " date is: " + stack.peek());
                        stack.add(date);
                        totalTime = 0;
                    }
                    totalTime += timeElapsed;
                }while(cursor.moveToNext());
            }

        }catch (Exception e){
            Log.e(TAG,"getBarChartBarData error: " + e.toString());
        }finally {
            if(!cursor.isClosed() && cursor != null){
                cursor.close();
            }
            if(!stack.isEmpty()) {
                BarEntry barEntry = new BarEntry(getDay(stack.peek()),totalTime);
                entries.add(barEntry);
            }

            BarDataSet barDataSet = null;

            if(entries != null){
                barDataSet = new BarDataSet(entries, "Weekly report");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            }
            return new BarData(barDataSet);
        }

    }

    private void insertTotalTime(TotalTime totalTime){
        Log.v(TAG,"addTotalTime...");

        ContentValues values = new ContentValues();
        values.put(TotalTime.TIME_ELAPSED_COLUMN, totalTime.getElapsedTime());
        values.put(TotalTime.TASK_COLUMN, totalTime.getTask());
        values.put(TotalTime.DATE_COLUMN, totalTime.getDate());

        getWritableDatabase().insert(TotalTime.TABLE, null, values);
        close();
    }

    private void updateTotalTime(TotalTime totalTime, long destinatedTotalTime){
        Log.v(TAG, "updateTotalTime...");

        ContentValues values = new ContentValues();
        values.put(TotalTime.TIME_ELAPSED_COLUMN, destinatedTotalTime);

        String selection = TotalTime.TASK_COLUMN + " LIKE ? AND " + TotalTime.DATE_COLUMN + " LIKE ?";
        String[] selectionArgs = {totalTime.getTask(), totalTime.getDate()};

        try {
            getWritableDatabase().update(
                    TotalTime.TABLE,
                    values,
                    selection,
                    selectionArgs
            );
        }catch (Exception e){
            Log.e(TAG,"updateTotalTime: " + e.toString());
        }
        close();
    }


    private int getTotalTime(TotalTime totalTime){//This doesn't return the time right sometimes
        Log.v(TAG, "getElapsedTime...");

        String[] projection = {
                TotalTime.TIME_ELAPSED_COLUMN
        };

        String selection = TotalTime.TASK_COLUMN + " = ? AND " + TotalTime.DATE_COLUMN + " = ?";
        String[] selectionArgs = {totalTime.getTask(), totalTime.getDate()};
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    TotalTime.TABLE,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    null
            );
        }catch (Exception e){
            Log.e(TAG,"getTotalTime error: " + e.toString());
        }

        if(cursor!= null && cursor.getCount() > 0 ){
            cursor.moveToFirst();
            int returnTotalTime = cursor.getInt(cursor.getColumnIndex(TotalTime.TIME_ELAPSED_COLUMN));
            if(!cursor.isClosed()) {
                cursor.close();
            }
            close();
            return returnTotalTime;
        }else{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            close();
            return 0;
        }
    }

    public void addColorTag(TaskColorTag taskColorTag){
        Log.v(TAG, "addColorTag...");
        int colorTagFrequency = getColorTagFrequency(taskColorTag);
        if(colorTagFrequency > 0){

            updateColorTagFrequency(taskColorTag, colorTagFrequency + 1);
        }else{
            insertColorTag(taskColorTag);
        }
    }

    private int getColorTagFrequency(TaskColorTag taskColorTag){
        Log.v(TAG,"getColorTagFrequency....");

        String[] projection = {
                TaskColorTag.FREQUENCY_COLUMN
        };

        String selection = TaskColorTag.TAG_COLUMN + " = ?";
        String[] selectionArgs = {taskColorTag.getTaskTag()};

        Cursor cursor = getReadableDatabase().query(
                TaskColorTag.TABLE,
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
            return cursor.getInt(cursor.getColumnIndex(TaskColorTag.FREQUENCY_COLUMN));
        }else{
            return 0;
        }
    }

    private void updateColorTagFrequency(TaskColorTag taskColorTag, int destinatedFrequency){
        Log.v(TAG,"updateColorTagFrequency... destinatedFrequency: " + destinatedFrequency );

        ContentValues values = new ContentValues();
        values.put(TaskColorTag.FREQUENCY_COLUMN, destinatedFrequency);

        String selection = TaskColorTag.TAG_COLUMN + " LIKE ?";
        String[] selectionArgs = {taskColorTag.getTaskTag()};

        try{
            getWritableDatabase().update(
                    TaskColorTag.TABLE,
                    values,
                    selection,
                    selectionArgs
            );
        }catch (Exception e){
            Log.e(TAG,"updateColorTagFrequency: " + e.toString());
        }
        close();
    }

    private void insertColorTag(TaskColorTag taskColorTag){
        ContentValues values = new ContentValues();
        values.put(TaskColorTag.COLOR_COLUMN, taskColorTag.getTaskColor());
        values.put(TaskColorTag.TAG_COLUMN, taskColorTag.getTaskTag());
        values.put(TaskColorTag.FREQUENCY_COLUMN, 1);

        getWritableDatabase().insert(TaskColorTag.TABLE, null, values);
        close();
    }

    public void updateColorTag(TaskColorTag oldTaskColorTag, TaskColorTag newTaskColorTag){
        Log.v(TAG, "updateColorTag...");

        ContentValues values = new ContentValues();
        values.put(TaskColorTag.COLOR_COLUMN, newTaskColorTag.getTaskColor());
        values.put(TaskColorTag.TAG_COLUMN, newTaskColorTag.getTaskTag());

        String selection = TaskColorTag.TAG_COLUMN + " LIKE ?";
        String[] selectionArgs = {oldTaskColorTag.getTaskTag()};

        try{
            getWritableDatabase().update(
                    TaskColorTag.TABLE,
                    values,
                    selection,
                    selectionArgs
            );
        }catch (Exception e){
            Log.e(TAG, "updateColorTag: " + e.toString());
        }
        close();
    }

    public List<TaskColorTag> getColorTagList(){
        Log.v(TAG,"getColorTagList....");
        LinkedList<TaskColorTag> colorTagList = new LinkedList<>();

        Cursor cursor = null;

        try{
            String[] columns = new String[]{TaskColorTag.TAG_COLUMN,TaskColorTag.COLOR_COLUMN,TaskColorTag.FREQUENCY_COLUMN};
            String selection = TaskColorTag.FREQUENCY_COLUMN + " > 0";

            cursor = getReadableDatabase().query(
                    TaskColorTag.TABLE,
                    columns,
                    selection,
                    null,
                    null,
                    null,
                    TaskColorTag.FREQUENCY_COLUMN + " DESC",
                    "7"
            );

            if(cursor.moveToFirst()){
                do{
                    TaskColorTag taskColorTag = new TaskColorTag();
                    taskColorTag.setTaskTag(cursor.getString(cursor.getColumnIndex(TaskColorTag.TAG_COLUMN)));
                    taskColorTag.setTaskColor(cursor.getInt(cursor.getColumnIndex(TaskColorTag.COLOR_COLUMN)));

                    colorTagList.add(taskColorTag);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }finally {
            return colorTagList;
        }
    }

    private String getLastMondayDate(){
        Calendar calendar = Calendar.getInstance();
        int diff = Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH,diff == 1 ? -6 : diff );
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.v(TAG,"getLastMondayDate: " + simpleDateFormat.format(calendar.getTime()));
        return simpleDateFormat.format(calendar.getTime());
    }

    private String getPresentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.v(TAG,"getPresentDate : " + simpleDateFormat.format(calendar.getTime()));
        return simpleDateFormat.format(calendar.getTime());
    }

    private float getDay(String date){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = simpleDateFormat.parse(date);
            calendar.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar.get(Calendar.DAY_OF_WEEK) - 1.5f;
    }

}
