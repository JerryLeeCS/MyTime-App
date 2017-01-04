package item;

/**
 * Created by jerrylee on 1/3/17.
 */

public class RecyclerViewItem {
    private String taskName;
    private String timeElapsed;

    public RecyclerViewItem(String taskName, String timeElapsed){
        this.taskName = taskName;
        this.timeElapsed = timeElapsed;
    }

    public String getTaskName(){
        return taskName;
    }

    public String getTimeElapsed(){
        return timeElapsed;
    }

}
