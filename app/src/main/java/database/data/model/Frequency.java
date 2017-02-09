package database.data.model;

/**
 * Created by jerrylee on 2/9/17.
 */

public class Frequency {

    public static final String TAG = Frequency.class.getSimpleName();
    public static final String TABLE = "FREQUENCY_TABLE";

    public static final String TASK_COLUMN = "TASK";
    public static final String FREQUENCY_COLUMN = "FREQUENCY";

    private String taskName;
    private int frequency;

    public String getTaskName(){
        return taskName;
    }

    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public int getFrequency(){
        return frequency;
    }

    public void setFrequency(int frequency){
        this.frequency = frequency;
    }

}
