package item;

import java.io.Serializable;

/**
 * Created by Jerry on 1/13/2017.
 */

public class DataItem implements Serializable {


    private String databaseID;
    private String taskName;
    private long elapsedTime;
    private String elapsedTimeString;
    private String startTime;
    private String endTime;
    private String date;

    public DataItem(){
    }

    public void setDatabaseID(String databaseID){
        this.databaseID = databaseID;
    }

    public String getDatabaseID(){
        return databaseID;
    }

    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public String getTaskName(){
        return taskName;
    }

    public void setElapsedTime(long elapsedTime){
        this.elapsedTime = elapsedTime;
    }

    public void setElapsedTime(String elapsedTimeString){
        this.elapsedTimeString = elapsedTimeString;
    }

    public long getElapsedTime(){
        return elapsedTime;
    }

    public String getElapsedTimeString(){
        return elapsedTimeString;
    }


    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public String getStartTime(){
        return startTime;
    }

    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

}
