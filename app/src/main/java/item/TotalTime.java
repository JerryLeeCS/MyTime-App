package item;

/**
 * Created by Jerry on 2/15/2017.
 */

public class TotalTime {
    private static final String TAG = TotalTime.class.getSimpleName();
    public static final String TABLE = "TOTALTIME_TABLE";

    public static final String ID_COLUMN = "_ID";
    public static final String TASK_COLUMN = "_TASK";
    public static final String TIME_ELAPSED_COLUMN = "TOTAL_TIME";
    public static final String DATE_COLUMN = "_DATE";

    private int timeID;
    private String task;
    private long totalTime;
    private String date;

    public void setTimeID(int timeID){
        this.timeID = timeID;
    }

    public int getTimeID(){
        return timeID;
    }

    public void setTask(String task){
        this.task = task;
    }

    public String getTask(){
        return task;
    }

    public void setTotalTime(long totalTime){
        this.totalTime = totalTime;
    }

    public long getElapsedTime(){
        return totalTime;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

}
