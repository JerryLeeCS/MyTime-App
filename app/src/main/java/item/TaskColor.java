package item;

/**
 * Created by jerrylee on 4/9/17.
 */

public class TaskColor {
    public static final String TAG = TaskColor.class.getSimpleName();
    public static final String TABLE = "COLOR_TABLE";

    public static final String ID_COLUMN = "_ID";
    public static final String TASK_COLUMN = "TASK_COL";
    public static final String COLOR_COLUMN = "COLOR_COL";

    private String taskName;
    private int taskColor;

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
}
