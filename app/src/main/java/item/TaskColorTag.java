package item;

import java.io.Serializable;

/**
 * Created by jerrylee on 4/9/17.
 */

public class TaskColorTag implements Serializable{
    public static final String TAG = TaskColorTag.class.getSimpleName();
    public static final String TABLE = "COLOR_TABLE";

    public static final String ID_COLUMN = "_ID";
    public static final String TASK_COLUMN = "TASK_COL";
    public static final String COLOR_COLUMN = "COLOR_COL";
    public static final String TAG_COLUMN = "TAG_COL";

    private String taskName;
    private int taskColor;
    private String taskTag;

    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public String getTaskName(){
        return taskName;
    }

    public void setTaskColor(int taskColor){
        this.taskColor = taskColor;
    }

    public int getTaskColor(){
        return taskColor;
    }

    public void setTaskTag(String taskTag){
        this.taskTag = taskTag;
    }

    public String getTaskTag(){
        return taskTag;
    }
}
