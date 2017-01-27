package item;

import java.util.List;

/**
 * Created by Jerry on 1/12/2017.
 */

public class DataModel {
    private String sectionTitle;
    private List<DataItem> itemList;

    public DataModel(){
    }

    public DataModel(String sectionTitle, List<DataItem> itemList){
        this.sectionTitle = sectionTitle;
        this.itemList = itemList;
    }

    public String getSectionTitle(){
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle){
        this.sectionTitle = sectionTitle;
    }

    public List<DataItem> getItemList(){
        return itemList;
    }

    public void setItemList(List<DataItem> itemList){
        this.itemList = itemList;
    }
}
