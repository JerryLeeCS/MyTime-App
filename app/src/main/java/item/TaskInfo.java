package item;

import java.io.Serializable;

/**
 * Created by jerrylee on 2/9/17.
 */

public class TaskInfo implements Serializable{

    public static final String TAG = TaskInfo.class.getSimpleName();
    public static final String TABLE = "TASK_INFO_TABLE";

    public static final String ID_COLUMN = "_ID";
    public static final String TASK_COLUMN = "TASK";
    public static final String TIME_ELAPSED_COLUMN = "TIME";
    public static final String START_TIME_COLUMN = "START_TIME";
    public static final String END_TIME_COLUMN = "END_TIME";
    public static final String DATE_COLUMN = "DATE";

    private String ID;
    private String taskName;
    private long elapsedTime;
    private String startTime;
    private String endTime;
    private String date;

    public String getID(){
        return ID;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public String getTaskName(){
        return taskName;
    }

    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public long getElapsedTime(){
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime){
        this.elapsedTime = elapsedTime;
    }

    public String getStartTime(){
        return startTime;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public Frequency getTaskAndTime(){
        Frequency frequency = new Frequency();
        frequency.setTaskName(taskName);

        return frequency;
    }
}
