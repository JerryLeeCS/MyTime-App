package item;

/**
 * Created by jerrylee on 1/3/17.
 */

public class RecyclerViewItem {
    private String taskName;
    private String timeElapsed;

    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public void setTimeElapsed(String timeElapsed){
        this.timeElapsed = timeElapsed;
    }

    public String getTaskName(){
        return taskName;
    }

    public String getTimeElapsed(){
        return timeElapsed;
    }

}
