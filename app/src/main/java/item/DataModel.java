package item;

import java.util.List;

import database.data.model.TaskInfo;

/**
 * Created by Jerry on 1/12/2017.
 */

public class DataModel {
    private String sectionTitle;
    private int totalTimeElapsed;
    private List<TaskInfo> itemList;

    public DataModel(){
    }

    public DataModel(String sectionTitle, List<TaskInfo> itemList){
        this.sectionTitle = sectionTitle;
        this.itemList = itemList;
    }

    public String getSectionTitle(){
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle){
        this.sectionTitle = sectionTitle;
    }

    public int getTotalTimeElapsed(){
        return totalTimeElapsed;
    }

    public void setTotalTimeElapsed(int totalTimeElapsed){
        this.totalTimeElapsed = totalTimeElapsed;
    }

    public List<TaskInfo> getItemList(){
        return itemList;
    }

    public void setItemList(List<TaskInfo> itemList){
        this.itemList = itemList;
    }
}
