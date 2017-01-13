package item;

/**
 * Created by Jerry on 1/13/2017.
 */

public class DatabaseInsertItem {

    private String taskName;
    private long elapsedTime;
    private String startTime;
    private String endTime;
    private String date;

    public DatabaseInsertItem(){
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

    public long getElapsedTime(){
        return elapsedTime;
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
