package item;

import java.io.Serializable;

/**
 * Created by jerrylee on 4/9/17.
 */

public class TaskColorTag implements Serializable{
    public static final String TAG = TaskColorTag.class.getSimpleName();
    public static final String TABLE = "COLOR_TABLE";

    public static final String ID_COLUMN = "_ID";
    public static final String COLOR_COLUMN = "COLOR_COL";
    public static final String TAG_COLUMN = "TAG_COL";
    public static final String FREQUENCY_COLUMN = "FREQUENCY_COL";

    private int tagColor;
    private String taskTag;
    private int tagFrequency;

    public void setTaskColor(int taskColor){
        this.tagColor = taskColor;
    }

    public int getTaskColor(){
        return tagColor;
    }

    public void setTaskTag(String taskTag){
        this.taskTag = taskTag;
    }

    public String getTaskTag(){
        return taskTag;
    }

    public void setTagFrequency(int tagFrequency){
        this.tagFrequency = tagFrequency;
    }

    public int getTagFrequency(){
        return tagFrequency;
    }
}
