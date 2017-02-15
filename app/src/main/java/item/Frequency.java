package item;

/**
 * Created by jerrylee on 2/9/17.
 */

public class Frequency {

    public static final String TAG = Frequency.class.getSimpleName();
    public static final String TABLE = "FREQUENCY_TABLE";

    public static final String ID_COLUMN = "ID";
    public static final String TASK_COLUMN = "TASK_COL";
    public static final String FREQUENCY_COLUMN = "FREQUENCY_COL";

    private int taskId;
    private String taskName;
    private int frequency;

    public int getTaskId(){
        return taskId;
    }

    public void setTaskId(int taskId){
        this.taskId = taskId;
    }

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
